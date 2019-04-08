# What's This

The backend for the What's This mobile app created for BSU's CS469 HCI

## Running Locally

You will need java, kotlin and gradle installed.

### Heroku Setup

You will need a Heroku account with a web application and postgresql database
attached to said application. You will also need an Amazon AWS account with a a valid S3
bucket that has public access permissions.

Follow the various official documentation provided by heroku and amazon to 
find the proper security credentials for the app to use.

You will need a .env file in the root folder with
the following values filled in

```
DATABASE_URL=database_url_credentials
AWS_ACCESS_KEY_ID=xxxxx
AWS_SECRET_ACCESS_KEY=yyyyyy
S3_BUCKET_NAME=zzzzzz
```

You will need to run all the files in the sql folder to properly
set up the postgresql database.

Finally, run a gradle build and the following
commands in the project directory

```sh
heroku login
heroku local
```

The app should now be deployed to localhost:5000/



