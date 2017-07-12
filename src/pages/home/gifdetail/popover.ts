import { Component } from '@angular/core';
import {NavController,ToastController, ViewController } from 'ionic-angular';
import { Transfer, FileUploadOptions, TransferObject } from '@ionic-native/transfer';
import { File } from '@ionic-native/file';
import { CustomService } from '../../../services/custom.service';

@Component({
    selector: 'page-popover',
    templateUrl : './popover.html'
})

export class PopOverComponent {

    constructor(public navCtrl : NavController,
                public toastCtrl : ToastController,
                public viewCtrl: ViewController,
                public transfer: Transfer,
                public file : File,
                public cs : CustomService){}

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

    download(){
        const fileTransfer: TransferObject = this.transfer.create();
        // this.cs.showLoader();
        // const imageLocation = `${cordova.file.applicationDirectory}www/assets/img/${image}`;
        fileTransfer.download( 'https://gola-gif-dev-store-cf.xpresso.me/R2luZ2Vy/595b4710650000870039250b.gif',this.file.applicationDirectory+'ginger'+'aa.gif').then((entry) => {
        //fileTransfer.download(url, cordova.file.externalRootDirectory + {{appName}} + 'filename')
        //this.cs.hideLoader();
        //this.downloadToast(); 
        },(error) => {
         alert('err');
        //  this.cs.hideLoader();
    });
  }
   
}