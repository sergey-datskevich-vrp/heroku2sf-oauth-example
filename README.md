# Heroku 2 Salesforce OAuth Example

A barebones Gradle app, which can easily be deployed to Heroku.

This application support the [Getting Started with Gradle on Heroku](https://devcenter.heroku.com/articles/getting-started-with-gradle-on-heroku) article - check it out.

The app is show case for OAuth 2 authorization flow between Saleforce-Yocova domain and custom Java web application.

## Prerequisites

Client Secret/Client ID are present and specified in .env file for local deployment and in config envs for heroku deployment.

APP_DOMAIN_URL is present and correctly configured on the Salesforce-Yocova side, in .env file and in the config envs on heroku platform.

## Running Locally

Make sure you have Java installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ git clone https://github.com/sergey-datskevich-vrp/heroku2sf-oauth-example.git
$ cd heroku2sf-oauth-example
$ ./gradlew stage
$ heroku local web
```

Your app should now be running on [localhost:8081](http://localhost:8081/).

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku main
$ heroku open
```

## Use case

Once the app is deployed you can visit the index page at ```https://{your_domain_here}/```  
On the index page there are 2 buttons referring to ```/securedPage1``` and ```/securedPage2```  
These pages are protected and only accessible after successful authorization via salesforce org.  
Once you are authorized it is possible to visit a special page ```/loginSuccess``` to get the username specified on the salesforce org.  

Steps:
1. Go to https://{your_domain_here}/
2. Click ```Page 1```
3. Click ```Login with Yocova```
4. Pass authorization on https://yocova.com domain
5. Find ```https://{your_domain_here}/securedPage1``` opened
6. Goto ```https://{your_domain_here}/loginSuccess```
7. Find Welcome page with you Yocova account username specified.

## Documentation

For more information about OAuth authorization on Salesforce orgs, see these official documentation:

- [Salesforce official articles](https://help.salesforce.com/articleView?id=sf.remoteaccess_oauth_web_server_flow.htm&type=5)

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)