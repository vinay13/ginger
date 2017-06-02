import { Component } from '@angular/core';
import { Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { AboutPage } from '../pages/about/about';
import { LoginPage } from '../pages/login/login.component';
import { IdiomComponent } from '../pages/idiom/idiom.component';


@Component({
  templateUrl: 'app.html',
})

export class MyApp {
  rootPage:any = IdiomComponent;

  constructor(platform: Platform, 
              statusBar: StatusBar, 
              splashScreen: SplashScreen,){
    platform.ready().then(() => {
      statusBar.styleDefault();
   //   splashScreen.hide();
    });
  }
}
