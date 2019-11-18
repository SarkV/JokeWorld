from flask import Flask
from flask_mongoalchemy import MongoAlchemy
from flask_bcrypt import Bcrypt
from flask_jwt_extended import JWTManager
from flask_mail import Mail

from .config import config_by_name

db = MongoAlchemy()
flask_bcrypt = Bcrypt()
jwt = JWTManager()
mail = Mail()

def create_app(config_name):
    app = Flask(__name__)
    app.config.from_object(config_by_name[config_name])
    db.init_app(app)
    jwt.init_app(app)
    mail.init_app(app)
    flask_bcrypt.init_app(app)
    return app

