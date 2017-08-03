import { Component } from '@angular/core';
import { NavController} from 'ionic-angular';
import { TOScomponent} from '../../../components/termsofservice/tos.ts';
import {AboutPage} from '../../about/about';
import { SocialSharing } from '@ionic-native/social-sharing';
import {CustomService} from '../../../services/custom.service';
@Component({
    selector : 'page-settings',
    templateUrl : 'settings.html'
})

export class SettingsComponent {

    ranval = false;
    constructor(public navCtrl : NavController,
                public cs : CustomService,
                private socialSharing: SocialSharing){}

    TOSfunc(){
        this.navCtrl.push(TOScomponent);
    }

    lessData(){
        this.ranval = true;
        alert('lessdata');
        localStorage.setItem('lessdata','true');
    }

    shareApp(){
      this.cs.showLoader();
      this.socialSharing.share("Explore best of GIF and share through social network",'gola',"","https://play.google.com/store/apps/details?id=com.mobigraph.xpresso")
        .then( () =>{
            this.cs.hideLoader();
        },
        () => { this.cs.hideLoader(); }) 
    }

    logout(){
         localStorage.removeItem('access_token');
         localStorage.removeItem('username');
       // localStorage.clear();
        this.navCtrl.push(AboutPage);
    }

}


