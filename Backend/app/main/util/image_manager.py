import os
from flask import url_for
from werkzeug.utils import secure_filename

def upload_image(image):
    if image and allowed_file(image.filename):
        # From flask uploading tutorial
        filename = secure_filename(image.filename)
        image.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        return url_for('uploaded_file', filename=filename)
    else:
        return -1
