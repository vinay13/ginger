import { Component } from '@angular/core';
import { GifDetailComponent } from '../../home/gifdetail/gifdetail.component';
import { NavController,NavParams,LoadingController,ToastController } from 'ionic-angular';
import { UploadGifService } from '../../../services/upload.service';
import { FileTransfer,FileUploadOptions,FileTransferObject } from  '@ionic-native/file-transfer';
import { File } from '@ionic-native/file';
import {CustomService} from '../../../services/custom.service';
import * as _ from 'underscore';
import {FormGroup,FormControl,Validators} from '@angular/forms';

@Component({
    selector : 'page-add-tags',
    templateUrl : 'add-tags.html'

})

export class AddTagsComponent {

    public gifurl : any;
    public uploadBtn;
    public selectedIdiom = 'Tamil';
    addtagsForm : FormGroup; 
    formgif: any;
    public gifyPath;
    constructor(public navCtrl: NavController,
                public loadingCtrl: LoadingController ,
                public toastCtrl: ToastController,
                public navparams: NavParams,
                public _uploadserv: UploadGifService,
                private file: File,
                private transfer: FileTransfer,
                public cs: CustomService){
                //  this.gifurl = this.navparams.get('gifpath');
                //  this.selectedIdiom = this.navparams.get('idiom');
                    this.gifurl = this.navparams.get('weburl') || this.navparams.get('gifpath') ;
                    this.gifyPath = this.navparams.get('imagePath');
                    this.addtagsForm = new FormGroup({
                         url : new FormControl(this.gifurl),
                         idiom : new FormControl(this.selectedIdiom),   
                         tags : new FormControl(["Movie","MovieStar"],[Validators.required])   
                    })
        }

    ngOnInit(){
        //  categories : new FormControl(["Movie"],[Validators.required]),
        // this.formgif =  {
        //     "url": this.gifurl,
        //     "idiom": "Hindi",
        //     "categories": ["Movie"],
        //     "tags": ["Movie","MovieStar"]
        // } 
    }              
              

    response;
    UploadGif(){
        alert('UploadGif is click');
        this.cs.showLoader();
        this._uploadserv.UploadGifsByUrl(this.addtagsForm.value)
            .subscribe( (res) => { this.response = res; this.presentToast(); this.cs.hideLoader(); this.navCtrl.push(GifDetailComponent,{'url':this.response});},
                        (err) => { this.cs.hideLoader(); alert(err);},
                        () => { console.log(this.response);})    
    }


  //  _.uniqueId()
  public data_response;
 // public base64Image;
  uploadGifviaGallery(){
     alert('uploadGifviaGallery is clicked');
    const fileTransfer: FileTransferObject = this.transfer.create();
       // let filename = _.uniqueId() + ".gif";
       let url = 'https://goladev.mobigraph.co/ginger/uploadGif';
       let uploadUrl = encodeURI(url);
        let options : FileUploadOptions  = {
            fileKey: 'file',
            fileName: this.gifurl.substr(this.gifurl.lastIndexOf('/')+1),
            mimeType: 'image/gif',
            chunkedMode: false,
            headers: {
                 'X-Gola-Access-Key':'AzG7Co20vVl7cBC4Cgi1rmp7w',
                 'Authorization' : 'Bearer'+' '+ localStorage.getItem('access_token')
            },
            params: {
                "gif":  'rewdsarewrew12.gif',
                "idiom": "Tamil",
                "tags": ['sarcasm']
            }
        }; 
         this.cs.showLoader();
         alert(this.gifurl);
        fileTransfer.upload(this.gifurl,uploadUrl, options, false)
            .then((result: any) => {
              this.data_response = result ; 
               alert('success');
              this.cs.hideLoader();
              this.navCtrl.push(GifDetailComponent);
          }).catch((error: any) => {
              console.log('upload err',error);
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

  UploadGifsT(){
        if(this.navparams.get('weburl')){
                   this.UploadGif();
                }
        if(this.navparams.get('gifpath')){
                    this.uploadGifviaGallery();
                }
  }

}