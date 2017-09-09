import { Component } from '@angular/core';
import {NavController,NavParams,ToastController, ViewController } from 'ionic-angular';
import { FileTransfer,FileUploadOptions,FileTransferObject } from  '@ionic-native/file-transfer';
import { File } from '@ionic-native/file';
import { CustomService } from '../../../services/custom.service';
import { SocialSharing } from '@ionic-native/social-sharing';
import { AppRate } from '@ionic-native/app-rate';
import { Market } from '@ionic-native/market';
import { EmailComposer } from '@ionic-native/email-composer';

@Component({
    selector: 'page-popover',
    templateUrl : './popover.html'
})

export class PopOverComponent {

    GIFurl;
    detailPage;
    constructor(public navCtrl : NavController,
                public toastCtrl : ToastController,
                public viewCtrl: ViewController,
                public transfer: FileTransfer,
                public file : File,
                public cs : CustomService,
                private socialSharing: SocialSharing,
                public navparams : NavParams,
                private appRate: AppRate,
                private market: Market,
                private emailComposer: EmailComposer){
                  this.GIFurl  = this.navparams.get('gifURL');
                  this.detailPage  = this.navparams.get('detailPage');
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

    shareApp(){
      this.cs.showLoader();
      this.socialSharing.share("Explore best of GIF and share through social network",'gola',"","https://play.google.com/store/apps/details?id=com.mobigraph.xpresso")
        .then( () =>{
            this.cs.hideLoader();
        },
        () => { this.cs.hideLoader(); }) 
    }
    
    Rateus(){
        this.market.open('com.mobigraph.xpresso');
    }

    ReportAbuse(){
         this.cs.showLoader();
        this.socialSharing.shareViaEmail('', 'Report Gif',['hello@xpresso.me'])
        .then( () =>{
            this.cs.hideLoader();
        },
        () => { this.cs.hideLoader(); }) 
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