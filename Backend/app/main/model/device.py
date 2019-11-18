from .. import db, flask_bcrypt
from .base_model import BaseModel

class Device(db.Document):

    deviceId = db.StringField()
    deviceModel = db.StringField()
    deviceToken = db.StringField()
    appVersion = db.StringField()

    def json(self):
        return {
            'deviceId' : self.deviceId,
            'deviceModel' : self.deviceModel,
            'deviceToken' : self.deviceToken,
            'appVersion': self.appVersion
        }
