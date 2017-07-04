import { Component } from '@angular/core';
import { Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { AboutPage } from '../pages/about/about';
import { LoginPage } from '../pages/login/login.component';
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

  constructor(platform: Platform, 
              statusBar: StatusBar, 
              splashScreen: SplashScreen,
              networkserv : NetworkService,
              public _loginserv : LoginService
             ){
                platform.ready().then(() => {
                statusBar.styleDefault(); 
                splashScreen.hide();
    });
    
    platform.resume.subscribe(() => {
        //call the api to check if user's email is login
        // this._loginserv.checkEmailActivated()
        //   .subscribe((data) => { this.emailverify = data},
        //               (err) => { console.log(err)})
    })
  }
}
