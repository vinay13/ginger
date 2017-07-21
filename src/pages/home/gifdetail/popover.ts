import { Component } from '@angular/core';
import {NavController,NavParams,ToastController, ViewController } from 'ionic-angular';
import { FileTransfer,FileUploadOptions,FileTransferObject } from  '@ionic-native/file-transfer';
import { File } from '@ionic-native/file';
import { CustomService } from '../../../services/custom.service';

@Component({
    selector: 'page-popover',
    templateUrl : './popover.html'
})

export class PopOverComponent {

    GIFurl;
    constructor(public navCtrl : NavController,
                public toastCtrl : ToastController,
                public viewCtrl: ViewController,
                public transfer: FileTransfer,
                public file : File,
                public cs : CustomService,
                public navparams : NavParams){
                  this.GIFurl  = this.navparams.get('gifURL');
                  console.log("GIFurl",this.GIFurl);
                }

     DownloadToast(){
        let toast = this.toastCtrl.create({
            message: 'GIF is saved in app Storage',
            duration: 3000
        });
        toast.present();
    }

    dismiss(){
        this.viewCtrl.dismiss();
    }

    flag(){
        alert('clicked flag');
    }

    download(){
        const fileTransfer: FileTransferObject = this.transfer.create();
        this.dismiss();
        fileTransfer.download( this.GIFurl,this.file.externalApplicationStorageDirectory  + Math.floor(Math.random()*90000) + 10000 +'.gif').then((entry) => {
        this.DownloadToast();
        console.log('download complete: ' + entry.toURL());
       
         },(error) => {
        console.log('error',error);
        });
    }
   
}