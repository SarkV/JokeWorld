from .. import db, flask_bcrypt
from passlib.hash import pbkdf2_sha256 as sha256
from datetime import datetime
from .base_model import BaseModel
from .device import Device

class User(BaseModel):
    """ User Model for storing user related details """
  #  query_class = UserQuery
    email = db.StringField(required=True)
    username = db.StringField(required=False)
    password = db.StringField(required=False)
    registeredDate = db.DateTimeField(required=True, default=datetime.utcnow())
    lastLoginDate = db.DateTimeField(required=True, default=datetime.utcnow())
    country = db.StringField(required=False)
    profileImage = db.StringField(required=False)
    deviceInfo = db.ListField(db.DocumentField(Device))
    language = db.StringField(required=True, default="en")
    tokenRecoverPassword = db.StringField(required=True, default=None)
    tokenRecoverDate = db.DateTimeField(required=True, default=None)
    privacyAcceptedVersion = db.IntField(required=True, default=0)
    notificationEnabled = db.BoolField(required=True, default=True)

    @staticmethod
    def generate_hash(password):
        return sha256.hash(password)

    def verify_hash(self, password):
        return sha256.verify(password, self.password)

    def json(self):
        return {
            '_id' : str(self.mongo_id),
            'email' : self.email,
            'username': self.username,
            'language': self.language,
            'registeredDate': self.registeredDate.strftime("%Y-%m-%d %H:%M:%S"),
            'country': getattr(self, 'country', None),
            'deviceInfo': list(map(lambda device: device.json(), self.deviceInfo)),
            'profileImage' : getattr(self, 'profileImage,', None),
            'notificationEnabled' : self.notificationEnabled,
            'privacyAcceptedVersion' : self.privacyAcceptedVersion,
            'updateDate': self.updateDate.strftime("%Y-%m-%d %H:%M:%S"),
            'action' : self.action
        }
