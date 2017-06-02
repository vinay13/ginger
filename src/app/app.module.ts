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
import { SocialSharing } from '@ionic-native/social-sharing'; 
import { Transfer } from '@ionic-native/transfer';

//third party imports 
import { SuperTabsModule } from 'ionic2-super-tabs';
import { Camera } from '@ionic-native/camera';
import { File } from '@ionic-native/file';

//custom component
import { ProgressBarComponent } from '../components/progress-bar/progress-bar';
import { NoInternetComponent } from '../components/noInternet/noInternet.component';

//import services 
import {HomeService} from '../services/home.service';
import {UploadGifService} from '../services/upload.service';
import {SearchService} from '../services/search.service';
import {ProfileService} from '../services/profile.service';
import {Configuration} from '../services/app.constant';
import {CustomService} from '../services/custom.service';
import {LoginService} from '../services/login.service';
import {NetworkService} from '../services/network.service';

//other imports
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Network } from '@ionic-native/network';
import { GooglePlus } from '@ionic-native/google-plus';
import { Facebook } from '@ionic-native/facebook';
//import { CloudSettings,CloudModule } from '@ionic/cloud-angular';

// const cloudSettings: CloudSettings = {
//   'core': {
//     'app_id': '700193'
//   },
//   'auth': {
//     'google': {
//       'webClientId': '802921815833-vi6nrrotqau2c7c436j55c04r520lr8r.apps.googleusercontent.com',
//       'scope': []
//     }
//   }
// }


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
    PopOverComponent,
    ProgressBarComponent,
    NoInternetComponent 
  ],
  imports: [
    HttpModule,
    BrowserModule,
    IonicModule.forRoot(MyApp),
 //   CloudModule.forRoot(cloudSettings),
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
    PopOverComponent,
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
    Camera,
    File,
    Transfer,
    SocialSharing,
    Network,
    GooglePlus,
    Facebook

  ]
})

export class AppModule {}
