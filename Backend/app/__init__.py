from flask_restful import Api
from flask import Blueprint
import app

blueprint = Blueprint('api', __name__)

api = Api(blueprint)

# Routes
from .main.controller import user_controller

api.add_resource(user_controller.UserRegistration, '/registration')
api.add_resource(user_controller.UserLogin, '/login')
api.add_resource(user_controller.TokenRefresh, '/autologin')
api.add_resource(user_controller.UserModify, '/modify')
api.add_resource(user_controller.PasswordRecoverEmail, '/passwordRecover')
api.add_resource(user_controller.PasswordRecoverPage, '/recoverPassword/<string:token>')
api.add_resource(user_controller.PasswordRecoverChange, '/changePassword')
