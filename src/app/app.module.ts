import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { HttpModule } from '@angular/http';
import { AboutPage } from '../pages/about/about';
import { LoginPage } from '../pages/login/login.component'; 
import { SignupComponent } from '../pages/login/signup/signup.component';
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
import { SocialSharing } from '@ionic-native/social-sharing'; 
import { Transfer } from '@ionic-native/transfer';
import { Page1Page } from "../pages/page1/page1";
import { Page2Page } from "../pages/page2/page2";
import { Page3Page } from "../pages/page3/page3";
import { MasonryModule } from 'angular2-masonry';
// import Masonry from 'masonry-layout'
//third party imports 
import { SuperTabsModule } from 'ionic2-super-tabs';
import { File } from '@ionic-native/file';
import { Keyboard } from '@ionic-native/keyboard';
import { Clipboard } from '@ionic-native/clipboard';
import { FileTransfer } from '@ionic-native/file-transfer';
import { Deeplinks } from '@ionic-native/deeplinks';

//custom component
import { ProgressBarComponent } from '../components/progress-bar/progress-bar';
import { NoInternetComponent } from '../components/noInternet/noInternet.component';
import { TOScomponent } from '../components/termsofservice/tos.ts';

//import services 
import {HomeService} from '../services/home.service';
import {UploadGifService} from '../services/upload.service';
import {SearchService} from '../services/search.service';
import {ProfileService} from '../services/profile.service';
import {Configuration} from '../services/app.constant';
import {CustomService} from '../services/custom.service';
import {LoginService} from '../services/login.service';
import {NetworkService} from '../services/network.service';
import {AppRateService} from '../services/apprate.service';


//pipe
import { EllipsisPipe } from '../components/ellipses.pipe';

//other imports
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Network } from '@ionic-native/network';
import { GooglePlus } from '@ionic-native/google-plus';
import { Facebook } from '@ionic-native/facebook';
import { AppRate } from '@ionic-native/app-rate';
import { FileChooser } from '@ionic-native/file-chooser';
import { FilePath } from '@ionic-native/file-path';

@NgModule({
  declarations: [
    MyApp,
    AboutPage,
    LoginPage,
    SignupComponent,
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
    PopOverComponent,
    ProgressBarComponent,
    NoInternetComponent ,
    Page1Page,
    Page2Page,
    Page3Page,
    TOScomponent,
    EllipsisPipe
  ],
  imports: [
    HttpModule,
    BrowserModule,
    IonicModule.forRoot(MyApp),
    SuperTabsModule.forRoot(),
    MasonryModule,
    
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    AboutPage,
    LoginPage,
    SignupComponent,
    HomeComponent,
    SearchComponent,
    GifDetailComponent,
    IdiomComponent,
    SearchResultComponent,
    UploadComponent,
    ProfileComponent,
    ProfileEditComponent,
    NoInternetComponent,
    SettingsComponent,
    AddTagsComponent,
    PopOverComponent,
    Page1Page,
    Page2Page,
    Page3Page,
    TOScomponent
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
    CustomService,
    NetworkService,
    File,
    FileChooser,
    FilePath,
    FileTransfer,
    SocialSharing,
    Clipboard,
    Network,
    GooglePlus,
    Facebook,
    Keyboard,
    AppRate,
    AppRateService,
    Deeplinks
  ]
})

export class AppModule {}
