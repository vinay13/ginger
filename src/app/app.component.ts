import { Component,ViewChild } from '@angular/core';
import { Platform,Nav,NavController,NavParams,ToastController } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { AboutPage } from '../pages/about/about';
import { LoginPage } from '../pages/login/login.component';
import { SignupComponent } from '../pages/login/signup/signup.component';
import { IdiomComponent } from '../pages/idiom/idiom.component';
import { NetworkService} from '../services/network.service';
import { NoInternetComponent} from '../components/noInternet/noInternet.component';
import { LoginService } from '../services/login.service';
import { Deeplinks } from '@ionic-native/deeplinks';
// import {NavController} from 'ionic-angular';
//import { GoogleAnalytics } from '@ionic-native/google-analytics';
import { Network } from '@ionic-native/network';
// import { Push, PushObject, PushOptions } from '@ionic-native/push';

@Component({
  templateUrl: 'app.html',
})

export class MyApp {
  rootPage:any;
  public emailverify;
   rootNavCtrl: NavController;
   @ViewChild(Nav) navChild:Nav;
   constructor(platform: Platform, 
              statusBar: StatusBar, 
              splashScreen: SplashScreen,
              networkserv : NetworkService,
              public toastCtrl : ToastController,
              public _loginserv : LoginService,
               public network : Network,
               private deeplinks : Deeplinks,
             ){
                platform.ready().then(() => {
                statusBar.styleDefault();
                splashScreen.hide();
                localStorage.setItem('lessdata','false');
                localStorage.setItem('tabIndex','0');
                if(localStorage.getItem('idiom') === null){
                   this.rootPage = IdiomComponent;
                }
                else{
                  this.rootPage = AboutPage;
                  //this.rootPage = NoInternetComponent;
                }

              //Deeplinks if from Ionic Native 
              // 'detail' : GifDetailPage
	            this.deeplinks.routeWithNavController(this.navChild, {
	              '/home': AboutPage,
	             }).subscribe((match) => {
	            console.log('Successfully routed', match);
	               }, (nomatch) => {
	            console.log('Unmatched Route', nomatch);
            });
            

    // this.push.register().then((t: PushToken) => {
    //     return this.push.saveToken(t);
    //  }).then((t: PushToken) => {
    //  console.log('Token saved:', t.token);
    // });
    // }
	    
   });


   
        let internetConnected = true;
        let disconnectSubscription = this.network.onDisconnect().subscribe(() => {
            
                if(!internetConnected) return;
                          internetConnected= false;
                            console.log('Offline');
                this.navChild.push(NoInternetComponent);
        });
                

        let connectSubscription = this.network.onConnect().subscribe(() => {
                
                if(internetConnected) return;
                  internetConnected= true;
                this.navChild.pop();
               
        });
                //connectSubscription.unsubscribe();
                // this.ga.startTrackerWithId('YOUR_TRACKER_ID')
                // .then(() => {
                //   console.log('Google analytics is ready now');
                //   this.ga.trackView('test');
                // })
                // .catch(e => console.log('Error starting GoogleAnalytics', e));
        
    
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
