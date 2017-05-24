import { Component } from '@angular/core';
import { GifDetailComponent } from '../../home/gifdetail/gifdetail.component';
import { NavController,NavParams,LoadingController,ToastController } from 'ionic-angular';
import { UploadGifService } from '../../../services/upload.service';
// import { Transfer , TransferObject } from  '@ionic-native/transfer';

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
                    this.gifurl = this.navparams.get('weburl') || this.navparams.get('gifpath') ;
                }

    ngOnInit(){
        this.formgif =  {
            "url": this.gifurl,
            "idiom": "Hindi",
            "categories": ["Movie"],
            "tags": ["Movie","MovieStar"]
        } 
    }              
              

    response;
    UploadGif(){
        this._uploadserv.UploadGifsByUrl(this.formgif)
            .subscribe( (res) => { this.response = res; this.navCtrl.push(GifDetailComponent,{'url':this.response.url});},
                    () => { console.log(this.response);})
        this.presentLoading();
        this.presentToast();   
    }


//   public data_response;
//   public base64Image;
//   uploadPic(){
//     const fileTransfer: TransferObject = this.transfer.create();
//     //let ft = new Transfer();
//         let filename = _.uniqueId() + ".jpg";
//         let options = {
//             fileKey: 'file',
//             fileName: filename,
//             mimeType: 'image/jpeg',
//             chunkedMode: false,
//             headers: {
//                 'Content-Type' : undefined
//             },
//             params: {
//                 "file": filename
//             }
//         }; 
      
//         fileTransfer.upload(this.base64Image, "https://yugma-testing.appspot.com/upload-file", options, false)
//         .then((result: any) => {
//            console.log('success');
//            this.data_response = result ; 
//            alert(result);
//         }).catch((error: any) => {
//             console.log(error);
//         }); 
//     }



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