import { Component } from '@angular/core';
import {NavController,ToastController, ViewController } from 'ionic-angular';


@Component({
    selector: 'page-popover',
    templateUrl : './popover.html'
})

export class PopOverComponent {

    constructor(public navCtrl : NavController,
                public toastCtrl : ToastController,
                public viewCtrl: ViewController){



                }

     presentToast(){
        let toast = this.toastCtrl.create({
            message: 'You are now Logout',
            duration: 3000
        });
        toast.present();
    }

    logout(){
        this.presentToast();
        this.viewCtrl.dismiss();
    }
   
}