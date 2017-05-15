import { Component } from '@angular/core';
import { GifDetailComponent } from '../../home/gifdetail/gifdetail.component';
import { NavController,LoadingController,ToastController } from 'ionic-angular';

@Component({
    selector : 'page-add-tags',
    templateUrl : 'add-tags.html'

})


export class AddTagsComponent {

    constructor(public navCtrl: NavController,
                public loadingCtrl: LoadingController ,
                public toastCtrl: ToastController){}

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