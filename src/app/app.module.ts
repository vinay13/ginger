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
import { Page4Page } from "../pages/page4/page4";
import { Page5Page } from "../pages/page5/page5";
import { Page6Page } from "../pages/page6/page6";
import { Page7Page } from "../pages/page7/page7";
import { Page8Page } from "../pages/page8/page8";
import { MasonryModule } from 'angular2-masonry';
// import Masonry from 'masonry-layout'
//third party imports 
import { SuperTabsModule } from 'ionic2-super-tabs';
import { File } from '@ionic-native/file';
import { Keyboard } from '@ionic-native/keyboard';
import { Clipboard } from '@ionic-native/clipboard';
import { FileTransfer } from '@ionic-native/file-transfer';
import { Deeplinks } from '@ionic-native/deeplinks';
import { Device } from '@ionic-native/device';

//custom component
import { ProgressBarComponent } from '../components/progress-bar/progress-bar';
import { NoInternetComponent } from '../components/noInternet/noInternet.component';
import { TOScomponent } from '../components/termsofservice/tos';
import { ErrorPage } from '../components/errorpage';
// import { PushNotification } from '../components/pushnotification';

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
import {NotificationService} from '../services/notification.service';

//pipe
import { EllipsisPipe } from '../components/ellipses.pipe';

//Directive
import {HideHeaderDirective} from '../components/hideheader';
import {HideFabDirective} from '../components/hidefab';
import {AccordionComponent} from '../components/accordion/accordion';
import {PreLoaderDirective} from '../components/preloader';
import {PlaceholderDirective} from '../components/placeholder';
import {NavbarTransitionDirective} from '../components/navbarTransition';
//other imports
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Network } from '@ionic-native/network';
import { GooglePlus } from '@ionic-native/google-plus';
import { Facebook } from '@ionic-native/facebook';
import { AppRate } from '@ionic-native/app-rate';
import { FileChooser } from '@ionic-native/file-chooser';
import { FilePath } from '@ionic-native/file-path';
import { CloudSettings, CloudModule } from '@ionic/cloud-angular';
import { Push, PushObject, PushOptions } from '@ionic-native/push';
import { OneSignal } from '@ionic-native/onesignal';



const cloudSettings: CloudSettings = {
  'core': {
    'app_id': '700193',
  },
  'push': {
    'sender_id': '835303737306',
    'pluginConfig': {
      'ios': {
        'badge': true,
        'sound': true
      },
      'android': {
        'iconColor': '#343434'
      }
    }
  }
};

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
    Page4Page,
    Page5Page,
    Page6Page,
    Page7Page,
    Page8Page,
    TOScomponent,
    EllipsisPipe,
    ErrorPage,
    HideHeaderDirective,
    HideFabDirective,
    AccordionComponent,
    PreLoaderDirective,
    PlaceholderDirective,
    NavbarTransitionDirective
  ],
  imports: [
    HttpModule,
    BrowserModule,
    IonicModule.forRoot(MyApp),
    SuperTabsModule.forRoot(),
    CloudModule.forRoot(cloudSettings),
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
    Page4Page,
    Page5Page,
    Page6Page,
    Page7Page,
    Page8Page,
    TOScomponent,
    ErrorPage
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
    NotificationService,
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
    Deeplinks,
    OneSignal,
    Push,
    Device
  ]
})

export class AppModule {}
