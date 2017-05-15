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
import { UploadComponent } from '../pages/upload/upload.component';
import { ProfileComponent } from '../pages/profile/profile.component';
import { ProfileEditComponent } from '../pages/profile/edit/profile-edit.component';
import { SettingsComponent } from '../pages/profile/settings/settings.component';
import { AddTagsComponent } from '../pages/upload/add-tags/add-tags.component';
import { PopOverComponent } from '../pages/home/gifdetail/popover';

//third party imports 
import { SuperTabsModule } from 'ionic2-super-tabs';

//import services 
import {HomeService} from '../services/home.service';
import {UploadGifService} from '../services/upload.service';
import {SearchService} from '../services/search.service';
import {ProfileService} from '../services/profile.service';
import {Configuration} from '../services/app.constant';
import {CustomService} from '../services/custom.service';
import {LoginService} from '../services/login.service';
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
    UploadComponent,
    ProfileComponent,
    ProfileEditComponent,   
    SettingsComponent,
    AddTagsComponent, 
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
    UploadComponent,
    ProfileComponent,
    ProfileEditComponent,
    SettingsComponent,
    AddTagsComponent,
    PopOverComponent
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    HomeService,
    SearchService,
    UploadGifService,
    ProfileService,
    LoginService,
    Configuration,
    CustomService

  ]
})

export class AppModule {}
