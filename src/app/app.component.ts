import { Component } from '@angular/core';
import { Platform,NavController,NavParams,ToastController } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { AboutPage } from '../pages/about/about';
import { LoginPage } from '../pages/login/login.component';
import { SignupComponent } from '../pages/login/signup/signup.component';
import { IdiomComponent } from '../pages/idiom/idiom.component';
import { NetworkService} from '../services/network.service';
import { NoInternetComponent} from '../components/noInternet/noInternet.component';
import { LoginService } from '../services/login.service';
// import {NavController} from 'ionic-angular';


@Component({
  templateUrl: 'app.html',
})

export class MyApp {
  rootPage:any = IdiomComponent;
  public emailverify;
   rootNavCtrl: NavController;
  constructor(platform: Platform, 
              statusBar: StatusBar, 
              splashScreen: SplashScreen,
              networkserv : NetworkService,
              public toastCtrl : ToastController,
              public _loginserv : LoginService
             ){
                platform.ready().then(() => {
                statusBar.styleDefault(); 
                splashScreen.hide();
      });
    
    platform.resume.subscribe(() => {
       
        this._loginserv.checkEmailActivated()
          .subscribe((data) => { this.emailverify = data, this.navSignup(data); },
                      (err) => { console.log(err)},
                      ()    => {console.log('verify email',this.emailverify);})
    })
  }


  navSignup(res){
    if(res.inactive == 'true' ){
        this.rootNavCtrl.push(SignupComponent);
        alert('Email is not verified..');
    }
  }
}
