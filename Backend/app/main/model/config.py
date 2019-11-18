from .. import db, flask_bcrypt
from passlib.hash import pbkdf2_sha256 as sha256
from datetime import datetime
from .base_model import BaseModel

class Config(BaseModel):
    """ User Model for storing user related details """
  #  query_class = UserQuery
    key = db.StringField(required=True)
    value = db.StringField(required=True)

    def json(self):
        return {
            'key' : self.key,
            'value': self.value,
            'updateDate': self.updateDate.strftime("%Y-%m-%d %H:%M:%S"),
            'action' : self.action
        }
