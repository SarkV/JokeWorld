from flask_restful import Resource, reqparse
from ..model.user import User
from ..model.config import Config
from ..service import user_service, config_service
import traceback
import logging
from ..util.response import Response, catchException
from ..util import image_manager
from ..util import utils
import datetime
from flask import render_template, make_response, request
from flask_jwt_extended import (create_access_token, create_refresh_token, jwt_required, jwt_refresh_token_required, get_jwt_identity, get_raw_jwt)

general_parser = reqparse.RequestParser(bundle_errors=False)

expires = datetime.timedelta(minutes=1)

class UserRegistration(Resource):
    def post(self):
        parser = general_parser.copy()
        parser.add_argument('username', required = True, trim=True)
        parser.add_argument('email', required = True, trim=True)
        parser.add_argument('password', required = True, trim=True)
        parser.add_argument('deviceId', required = True, trim=True)
        parser.add_argument('deviceModel', required = True, trim=True)
        parser.add_argument('deviceToken', required = True, trim=True)
        parser.add_argument('appVersion', required = True, trim=True)
        parser.add_argument('language', required = False, default="en", trim=True)
        try:
            data = parser.parse_args()
            if not data["username"] or not data["email"] or not data["password"] or not data["deviceId"] or not data["deviceModel"] or not data["deviceToken"] or not data["appVersion"]:
                return Response(400).__dict__
            result = user_service.register(
                email=data["email"],
                password=data["password"],
                username=data["username"],
                deviceId=data["deviceId"],
                deviceModel=data["deviceModel"],
                deviceToken=data["deviceToken"],
                appVersion=data["appVersion"],
                language=data["language"]
            )
            if result == 1:
                new_user = user_service.find_user(data['email'])
                config = config_service.getConfig()
                access_token = create_access_token(identity = str(new_user.mongo_id), expires_delta=expires)
                refresh_token = create_refresh_token(identity = str(new_user.mongo_id), expires_delta=False)
                expired_date = datetime.datetime.utcnow() + expires
                return Response(200, user=new_user.json(), access_token=access_token, refresh_token=refresh_token, expired_date=expired_date.strftime("%Y-%m-%d %H:%M:%S"), config=utils.list_to_json(config)).__dict__
            elif result == -1:
                return Response(406).__dict__
            else:
                return Response(409).__dict__
        except Exception as e:
            return catchException(e)

class UserLogin(Resource):
    def post(self):
        parser = general_parser.copy()
        parser.add_argument('email', required = True)
        parser.add_argument('password', required = True)
        parser.add_argument('deviceId', required = True)
        parser.add_argument('deviceModel', required = True)
        parser.add_argument('deviceToken', required = True)
        parser.add_argument('appVersion', required = True)
        parser.add_argument('language', required = False, default="en")
        try:
            data = parser.parse_args()
            result = user_service.login(
                email=data['email'],
                password=data['password'],
                deviceId=data['deviceId'],
                deviceModel=data['deviceModel'],
                deviceToken=data['deviceToken'],
                appVersion=data['appVersion'])
            if result == -1:
                return Response(401).__dict__
            else:
                config = config_service.getConfig()
                access_token = create_access_token(identity = str(result.mongo_id), expires_delta=expires)
                refresh_token = create_refresh_token(identity = str(result.mongo_id), expires_delta=False)
                expired_date = datetime.datetime.utcnow() + expires
                return Response(200, user=result.json(), config=utils.list_to_json(config), access_token=access_token, refresh_token=refresh_token, expired_date=expired_date.strftime("%Y-%m-%d %H:%M:%S")).__dict__
        except Exception as e:
            return catchException(e)

class UserModify(Resource):
    @jwt_required
    def post(self):
        parser = general_parser.copy()
        parser.add_argument('language', required = False)
        parser.add_argument('country', required = False)
        parser.add_argument('password', required = False)
        try:
            data = parser.parse_args()
            print(data)
            user_id = get_jwt_identity()
            user = user_service.modify_user(user_id, data)
            if user == -1:
                return Response(304).__dict__
            else:
                return Response(200, user=user.json()).__dict__
        except Exception as e:
            return catchException(e)

class ChangeProfilePicture(Resource):
    @jwt_required
    def post(self):
        parser = general_parser.copy()
        parser.add_argument('file', type=werkzeug.datastructures.FileStorage, location='files')
        try:
            data = change_picture_parser.parse_args()
            current_user = get_jwt_identity()

        except Exception as e:
            return catchException(e)

class TokenRefresh(Resource):
    @jwt_refresh_token_required
    def post(self):
        try:
            user_id = get_jwt_identity()
            user = user_service.get_user(user_id)

            access_token = create_access_token(identity = user_id)
            return Response(200, user=user.json(), access_token=access_token).__dict__
        except Exception as e:
            return catchException(e)

class PasswordRecoverEmail(Resource):
    def post(self):
        parser = general_parser.copy()
        parser.add_argument('email', required = True)
        try:
            data = parser.parse_args()
            url_base = request.base_url.replace(request.path, "/recoverPassword/")
            user_service.sendPasswordToken(url=url_base , email=data['email'])

            return Response(200).__dict__
        except Exception as e:
            return catchException(e)

class PasswordRecoverChange(Resource):
    def post(self):
        parser = general_parser.copy()
        parser.add_argument('email', required = True)
        parser.add_argument('password', required = True)
        parser.add_argument('token', required = True)
        try:
            data = parser.parse_args()
            url_base = request.base_url.replace(request.path, "/recoverPassword/")
            user_service.resetPassword(email=data['email'], password=data['password'], token=data['token'])
            print(data)
            headers = {'Content-Type': 'text/html'}
            return make_response(render_template('resetPasswordOk.html'), 200, headers)
        except Exception as e:
            return catchException(e)

class PasswordRecoverPage(Resource):
    def get(self, token):
        try:
            headers = {'Content-Type': 'text/html'}
            return make_response(render_template('resetPassword.html', token=token ), 200, headers)
        except Exception as e:
            return catchException(e)


