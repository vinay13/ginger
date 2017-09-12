import { Component, Input, Output,EventEmitter, OnChanges} from '@angular/core';
import { Nav,NavController,Events} from 'ionic-angular';
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
    isSelected = false;
    constructor(public navCtrl : NavController,
                public nav : Nav,
                public cs : CustomService,
                private socialSharing: SocialSharing,
                public events: Events){
                    if(localStorage.getItem('lessdata') == 'true'){
                        this.isSelected = true;
                    }
                }

    TOSfunc(){
        this.navCtrl.push(TOScomponent);
    }

    lessData(){
        this.ranval = true;
        alert('lessdata');
        localStorage.setItem('lessdata','true');
    }

    poptoHome(){
        //this.navCtrl.setRoot(AboutPage);
         this.nav.popToRoot();
    }

    device:number = 1;
    ToggleChange(e:Event) {
        
        this.isSelected =  !this.isSelected;
        // alert(this.isSelected);
        localStorage.setItem('lessdata',this.isSelected.toString())
        this.events.publish('lessdata:created', this.isSelected);
    }

    shareApp(){
      this.cs.showLoader();
      this.socialSharing.share("Must try",'gola',"","https://play.google.com/store/apps/details?id=com.mobigraph.gola")
        .then( () =>{
            this.cs.hideLoader();
        },
        () => { this.cs.hideLoader(); }) 
    }

    logout(){
         localStorage.removeItem('access_token');
         localStorage.removeItem('username');
       // localStorage.clear();
         this.nav.popToRoot();
    }
}


