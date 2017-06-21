import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopOverComponent } from './popover';
import { SearchComponent } from '../../search/search.component';
import { SearchResultComponent } from '../../search/searchResult/search-result.component';
import { NavController, ToastController , NavParams } from 'ionic-angular';
import { SocialSharing } from '@ionic-native/social-sharing';
import { CustomService } from '../../../services/custom.service';
import { HomeService } from '../../../services/home.service';
import { Transfer, FileUploadOptions, TransferObject } from '@ionic-native/transfer';
import { File } from '@ionic-native/file';
import { ProfileComponent } from '../../profile/profile.component';

@Component({
    selector : 'page-gifdetail',
    templateUrl : 'gifdetail.html'
})

export class GifDetailComponent {

    public gifurl;
    public gifobject;
    public recomns : any;
    public tagslist;
    public selectedIdiom;
    constructor(public popoverCtrl : PopoverController,
                public navCtrl : NavController,
                public toastCtrl : ToastController,
                private socialSharing: SocialSharing,
                private _homeserv: HomeService,
                private cs : CustomService,
                private transfer: Transfer, 
                private file: File,
                public navparams : NavParams){
                    this.gifobject = this.navparams.get('url');
                     this.selectedIdiom = this.navparams.get('idiom');
                    this.gifurl =  this.gifobject.url;
                    this.RecommendedGifs();
                    this.tagslist = this.gifobject.tags;
                }

    loadProgress: number = 0;
    public hidebar = false;	            
    ionViewDidLoad(){

		setInterval(() => {

			if(this.loadProgress < 100){
				this.loadProgress++;
                
                if(this.loadProgress == 10){
                    this.gifurl = this.gifobject.url;
                }

                if(this.loadProgress == 30)
                {
                    console.log(this.gifurl);
                    this.gifurl = this.gifobject.url;
                    console.log(this.gifurl);
                }

                if(this.loadProgress == 99)
                {
                    this.hidebar = true;
                }
			}

		}, 70);

	} 

    
           

    public totalcount;
    RecommendedGifs(){
        this._homeserv.getRelatedGifs(this.gifobject.idiom,this.gifobject.id)
            .subscribe( (res) => {this.recomns = res.contents, this.totalcount = res.totalCount},
                        (err) => console.log(err),
                        () => console.log('related gifs',this.totalcount))
    }

    presentPopover(myEvent){
        let popover = this.popoverCtrl.create(PopOverComponent);
        popover.present({
            ev: myEvent
        });
    }

    TagClicked(tag){
        this.navCtrl.push(SearchResultComponent,{
            'tag' : tag,
            'idiom': this.selectedIdiom
        });
    }

    searchButton(){
      this.navCtrl.push(SearchComponent);
    }

    userProfile(){
        this.navCtrl.push(ProfileComponent);
    }

    GIFviewer(url){
        this.navCtrl.push(GifDetailComponent,{
            'url': url
        });   
    }

    public favorite = false;
    Addfavorites(){
       this.favorite = true;
       this.presentToast();
    }

    copyUrl(gifurl){
        console.log('Url Copied');
        console.log('copyURl',gifurl);
        let toast = this.toastCtrl.create({
            message : 'URL copied to clipboard',
            duration : 3000
        });
        toast.present();
    }

    presentToast(){
        let toast = this.toastCtrl.create({
            message: 'Added to favorites list',
            duration: 3000
        });
    toast.present();
}

    downloadToast(){
        let toast = this.toastCtrl.create({
            message: 'GIF Saved!',
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

  shareGifTwitter(){
      this.cs.showLoader();
    this.socialSharing.shareViaTwitter("message",this.gifurl,"https://giphy.com")
        .then(()=>{
            this.cs.hideLoader();
        },
        ()=>{
         alert("failed")
         this.cs.hideLoader();
      })
  }

  shareGifFacebook(){
      this.cs.showLoader();
      this.socialSharing.shareViaFacebook("message",this.gifurl,"https://giphy.com")
        .then(() => {
            this.cs.hideLoader();
        },
        () => { alert("U don't have facebook app"); this.cs.hideLoader(); } )
  }


  shareGifInstagram(){
      this.cs.showLoader();
      this.socialSharing.shareViaInstagram("message", this.gifurl)
            .then(() => {
                this.cs.hideLoader();
            },
            ()=> { alert("U don't have Instagram app"); this.cs.hideLoader(); })
  }


  download() {
  const fileTransfer: TransferObject = this.transfer.create();
  this.cs.showLoader();
    // const imageLocation = `${cordova.file.applicationDirectory}www/assets/img/${image}`;
   fileTransfer.download( this.gifurl,this.file.applicationDirectory+'ginger'+'aa.gif').then((entry) => {
   //fileTransfer.download(url, cordova.file.externalRootDirectory + {{appName}} + 'filename')
    this.cs.hideLoader();
    this.downloadToast(); 
  }, (error) => {
    alert('err');
    this.cs.hideLoader();
  });
  }
}