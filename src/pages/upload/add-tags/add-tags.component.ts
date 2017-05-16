import { Component } from '@angular/core';
import { GifDetailComponent } from '../../home/gifdetail/gifdetail.component';
import { NavController,NavParams,LoadingController,ToastController } from 'ionic-angular';

@Component({
    selector : 'page-add-tags',
    templateUrl : 'add-tags.html'

})


export class AddTagsComponent {

    public gifurl : any;
    constructor(public navCtrl: NavController,
                public loadingCtrl: LoadingController ,
                public toastCtrl: ToastController,
                public navparams : NavParams){
                   this.gifurl = this.navparams.get('gifpath');
                }

    UploadGif(){
        this.presentLoading();
        this.presentToast();
        this.navCtrl.push(GifDetailComponent);
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