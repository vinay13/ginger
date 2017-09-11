import { Injectable } from '@angular/core';
import {AppRate} from '@ionic-native/app-rate';
import { Platform } from 'ionic-angular';


@Injectable()
export class AppRateService{

    appRate : any = AppRate;  

    constructor(public platform : Platform){
        this.platform.ready().then(
            () =>  this.appRate.preferences.storeAppURL = {
            android : 'market://details?id=com.mobigraph.gola'
            }
        )
    }

}
