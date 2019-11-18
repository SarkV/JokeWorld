from .. import db, flask_bcrypt
from datetime import datetime

class BaseModel(db.Document):

    createdDate = db.DateTimeField(required=True, default=datetime.utcnow())
    updateDate = db.DateTimeField(required=True, default=datetime.utcnow())
    action = db.StringField(required=True, default="INSERTED")

    def save(self, *args, **kwargs):
        if not hasattr(self, 'mongo_id'):
            self.startDate = datetime.utcnow()
            self.action = "INSERTED"
        else:
            self.action = "UPDATED"
        self.updateDate = datetime.utcnow()
        return super(BaseModel, self).save(*args, **kwargs)

    def remove(self, *args, **kwargs):
        self.action = "DELETED"
        self.updateDate = datetime.utcnow()
        return super(BaseModel, self).save(*args, **kwargs)
