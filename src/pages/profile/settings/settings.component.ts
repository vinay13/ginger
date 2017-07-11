import { Component } from '@angular/core';
import { NavController} from 'ionic-angular';
import { TOScomponent} from '../../../components/termsofservice/tos.ts';

@Component({
    selector : 'page-settings',
    templateUrl : 'settings.html'
})

export class SettingsComponent {

    constructor(public navCtrl : NavController){}

    TOSfunc(){
        this.navCtrl.push(TOScomponent);
    }

    logout(){
        localStorage.removeItem('access_token');
        this.navCtrl.pop();
    }

}


