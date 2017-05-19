import { Component } from '@angular/core';
import { ProfileService } from '../../../services/profile.service';
import {NavController , NavParams} from 'ionic-angular';

@Component({
    selector : 'page-profile-edit',
    templateUrl : 'profile-edit.html'
})

export class ProfileEditComponent {

    public userInfo;
    constructor(public proServ : ProfileService,
                public navparams : NavParams ){
      // this.getUserInfo();
        this.userInfo = this.navparams.get('data')
       console.log('user',this.userInfo);
    }

    getUserInfo(){
        this.proServ.GetUserProfile()
        .subscribe( (data) => { this.userInfo = data},
                        () =>  console.log('userInfo',this.userInfo))
    }


}