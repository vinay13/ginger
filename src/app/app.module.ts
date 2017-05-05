import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';

import { AboutPage } from '../pages/about/about';
import { LoginPage } from '../pages/login/login.component'; 
import { HomeComponent } from '../pages/home/home.component';
import { SearchComponent } from '../pages/search/search.component';
import { GifDetailComponent } from '../pages/home/gifdetail/gifdetail.component';

//third party imports 
import { SuperTabsModule } from 'ionic2-super-tabs';

//import services 
import {CommonService} from '../services/common.service';
import {HomeService} from '../services/home.service';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';


@NgModule({
  declarations: [
    MyApp,
    AboutPage,
    LoginPage,
    HomeComponent,
    SearchComponent,
    GifDetailComponent,
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    SuperTabsModule.forRoot()
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    AboutPage,
    LoginPage,
    HomeComponent,
    SearchComponent,
    GifDetailComponent

  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    CommonService,
    HomeService
  ]
})

export class AppModule {}
