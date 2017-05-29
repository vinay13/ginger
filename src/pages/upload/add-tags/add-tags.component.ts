import { Component } from '@angular/core';
import { GifDetailComponent } from '../../home/gifdetail/gifdetail.component';
import { NavController,NavParams,LoadingController,ToastController } from 'ionic-angular';
import { UploadGifService } from '../../../services/upload.service';
import { Transfer , TransferObject } from  '@ionic-native/transfer';
import { File } from '@ionic-native/file';
import {CustomService} from '../../../services/custom.service';
import * as _ from 'underscore';

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
                public navparams: NavParams,
                public _uploadserv: UploadGifService,
                private file: File,
                public transfer: Transfer,
                public cs: CustomService){
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
        this.cs.showLoader();
        this._uploadserv.UploadGifsByUrl(this.formgif)
            .subscribe( (res) => { this.response = res; this.presentToast(); this.cs.hideLoader(); this.navCtrl.push(GifDetailComponent,{'url':this.response});},
                        (err) => { this.cs.hideLoader(); alert(err);},
                        () => { console.log(this.response);})    
    }


  //  _.uniqueId()


  public data_response;
 // public base64Image;
  uploadGifviaGallery(){
    const fileTransfer: TransferObject = this.transfer.create();
    //let ft = new Transfer();
        let filename = _.uniqueId() + ".gif";
      
        let options = {
            fileKey: 'file',
            fileName: filename,
            mimeType: 'image/gif',
            chunkedMode: false,
            headers: {
            },
            params: {
                "gif": filename,
                "idiom": "hindi",
                "categories": ["Movie"],
                "tags": ["Movie","MovieStar"]
            }
        }; 
       alert(filename);
      // alert(this.gifurl);
      this.cs.showLoader();
      
        fileTransfer.upload(this.gifurl,'https://violet.mobigraph.co/ginger/uploadGif', options, false)
            .then((result: any) => {
              console.log('success');
              this.data_response = result ; 
              this.cs.hideLoader();
              this.navCtrl.push(GifDetailComponent);
              alert('success');
          }).catch((error: any) => {
                alert('error'+JSON.stringify(error));
               this.cs.hideLoader();
        }); 
     }


    presentToast(){
        let toast = this.toastCtrl.create({
            message: 'Uploaded successfully',
            duration: 3000
        });
    toast.present();
  }

}