import { Component } from '@angular/core';
import { GifDetailComponent } from '../../home/gifdetail/gifdetail.component';
import { NavController,NavParams,LoadingController,ToastController } from 'ionic-angular';
import { UploadGifService } from '../../../services/upload.service';

@Component({
    selector : 'page-add-tags',
    templateUrl : 'add-tags.html'

})


export class AddTagsComponent {

    public gifurl : any;
    formgif: any;
    constructor(public navCtrl: NavController,
                public loadingCtrl: LoadingController ,
                public toastCtrl: ToastController,
                public navparams : NavParams,
                public _uploadserv : UploadGifService ){
                //    this.gifurl = this.navparams.get('gifpath');
                    this.gifurl = this.navparams.get('weburl');
                }

    ngOnInit(){
        this.formgif =  {
            "url": "https://media.giphy.com/media/l4FGoVDgNCpllIfSw/giphy.gif",
            "idiom": "Tamil",
            "categories": ["NBA"],
            "tags": ["NBA"]
        } 
    }              
              

    response;
    UploadGif(){
        this._uploadserv.UploadGifsByUrl(this.formgif)
            .subscribe( (res) => { this.response = res; this.navCtrl.push(GifDetailComponent);},
                    () => { console.log(this.response);})
        this.presentLoading();
        this.presentToast();   
    }



    presentLoading(){
        let loader = this.loadingCtrl.create({
            duration: 3000
        });
        loader.present();
    }

    presentToast(){
        let toast = this.toastCtrl.create({
            message: 'Uploaded successfully',
            duration: 3000
        });
    toast.present();
  }

}