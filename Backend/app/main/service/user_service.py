import uuid
from datetime import datetime, timedelta

from .. import db, mail
from ..model.user import User
from ..model.device import Device
from flask_mail import Message
from flask import render_template
from ..util.utils import generate_token

def register(email, password, username, deviceId, deviceModel, deviceToken, appVersion, language):
    email = email.lower()
    user = User.query.filter_by(email=email).first()
    if not user:
        user = User.query.filter_by(username=username).first()
        if not user:
            device = Device(deviceId=deviceId, deviceModel=deviceModel, deviceToken=deviceToken, appVersion=appVersion)
            new_user = User(
                email=email,
                username=username,
                password=User.generate_hash(password),
                language=language,
                deviceInfo=[device]
            )
            new_user.save()
            return 1
        else:
            return -1
    else:
        return -2

def find_user(email):
    if email:
        return User.query.filter_by(email=email.lower()).first()
    else:
        return None

def get_user(id):
    return User.query.get(id)

def login(email, password, deviceId, deviceModel, deviceToken, appVersion):
    email = email.lower()
    user = User.query.filter_by(email=email).first()
    if user:
        if user.verify_hash(password):
            user.lastLoginDate = datetime.utcnow()
            actual_device = Device(deviceId=deviceId, deviceModel=deviceModel, deviceToken=deviceToken, appVersion=appVersion)
            for i in range(len(user.deviceInfo)):
                if user.deviceInfo[i].deviceId == actual_device.deviceId:
                    user.deviceInfo[i] = actual_device
                    actual_device = None
                    break;
            if actual_device is not None:
                user.deviceInfo.append(actual_device)
            user.save()
            return user
        else:
            return -1
    else:
        return -1


def modify_user(id, data):
    user = User.query.get(id)
    if user:
        for key in data:
            if data[key]:
                if key == 'password':
                    user.password = User.generate_hash(data[key])
                else:
                    setattr(user, key, data[key])
        user.save()
        return user
    else:
        return -1


def get_all_users():
    return {'users': list(map(lambda user: user.to_json(), User.query.all()))}

def get_a_user(username):
    return User.query.filter_by(username=username).first()


def verify_hash(user, password):
    return user.verify_hash(password)


def sendPasswordToken(url, email):
    user = User.query.filter_by(email = email).first()
    if(user is not None):
        token = generate_token(30)
        msg = Message("jokeworld - Password Reset",
                      sender="jokeworld",
                      recipients=[email])
        password_reset_url = url+token

        msg.html = render_template('passwordEmail.html', url=password_reset_url)
        mail.send(msg)
        user.save()
        user.tokenRecoverDate = datetime.utcnow() + timedelta(days=1)
        user.tokenRecoverPassword = token
        user.save()


def resetPassword(email, password, token):
    user = User.query.filter({"$and": [
                        {"email" : email},
                        {"tokenRecoverPassword" : token}
                        ]}).first()
    if(user is not None and user.tokenRecoverDate > datetime.utcnow()):
        user.password = User.generate_hash(password)
        user.tokenRecoverDate = None
        user.tokenRecoverPassword = None
        user.save()
        return 1
    else:
        return -1

