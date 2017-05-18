import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopOverComponent } from './popover';
import { SearchResultComponent } from '../../search/searchResult/search-result.component';
import { NavController, ToastController , NavParams } from 'ionic-angular';
import { SocialSharing } from '@ionic-native/social-sharing';
import { CustomService } from '../../../services/custom.service';

@Component({
    selector : 'page-gifdetail',
    templateUrl : 'gifdetail.html'
})

export class GifDetailComponent {

    public gifurl;
    constructor(public popoverCtrl : PopoverController,
                public navCtrl : NavController,
                public toastCtrl : ToastController,
                private socialSharing: SocialSharing,
                private cs : CustomService,
                public navparams : NavParams){
                    
                    this.gifurl = this.navparams.get('url');
                }

    presentPopover(){
        let popover = this.popoverCtrl.create(PopOverComponent);
        popover.present();
    }

    TagClicked(tag){
        this.navCtrl.push(SearchResultComponent,{
            'tag' : tag
        });
    }

    Addfavorites(){
       this.presentToast();
    }

    presentToast(){
        let toast = this.toastCtrl.create({
            message: 'Added to favorites list',
            duration: 3000
        });
    toast.present();
    }

   shareGif(){
    this.cs.showLoader();
    this.socialSharing.shareViaWhatsApp("Message via WhatsApp",this.gifurl, "https://giphy.com")
      .then(()=>{
        this.cs.hideLoader();
      },
      ()=>{
         alert("failed")
      })
  }
  
}