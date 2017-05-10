import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { HttpModule } from '@angular/http';


import { AboutPage } from '../pages/about/about';
import { LoginPage } from '../pages/login/login.component'; 
import { HomeComponent } from '../pages/home/home.component';
import { SearchComponent } from '../pages/search/search.component';
import { GifDetailComponent } from '../pages/home/gifdetail/gifdetail.component';
import { IdiomComponent } from '../pages/idiom/idiom.component';
import { SearchResultComponent } from '../pages/search/searchResult/search-result.component';
import { PopOverComponent } from '../pages/home/gifdetail/popover';

//third party imports 
import { SuperTabsModule } from 'ionic2-super-tabs';

//import services 
import {CommonService} from '../services/common.service';
import {HomeService} from '../services/home.service';
import {UploadGifService} from '../services/upload.service';
import {SearchService} from '../services/search.service';

//other imports
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
    IdiomComponent,
    SearchResultComponent,
    PopOverComponent
  ],
  imports: [
    HttpModule,
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
    GifDetailComponent,
    IdiomComponent,
    SearchResultComponent,
    PopOverComponent

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
