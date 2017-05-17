import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopOverComponent } from './popover';
import { SearchResultComponent } from '../../search/searchResult/search-result.component';
import { NavController, ToastController } from 'ionic-angular';
import { SocialSharing } from '@ionic-native/social-sharing';
import { CustomService } from '../../../services/custom.service';

@Component({
    selector : 'page-gifdetail',
    templateUrl : 'gifdetail.html'
})


export class GifDetailComponent {

    constructor(public popoverCtrl : PopoverController,
                public navCtrl : NavController,
                public toastCtrl : ToastController,
                private socialSharing: SocialSharing,
                private cs : CustomService){}


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
    this.socialSharing.shareViaWhatsApp("Message via WhatsApp","https://media.giphy.com/media/3oKIPkprNwpNTbccQ8/giphy.gif", "https://giphy.com")
      .then(()=>{
        this.cs.hideLoader();
      },
      ()=>{
         alert("failed")
      })
  }
 
    
}