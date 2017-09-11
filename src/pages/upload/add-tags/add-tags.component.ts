import { Component } from '@angular/core';
import { GifDetailComponent } from '../../home/gifdetail/gifdetail.component';
import { NavController,NavParams,LoadingController,ToastController } from 'ionic-angular';
import { UploadGifService } from '../../../services/upload.service';
import { FileTransfer,FileUploadOptions,FileTransferObject } from  '@ionic-native/file-transfer';
import { File } from '@ionic-native/file';
import {CustomService} from '../../../services/custom.service';
import * as _ from 'underscore';
import {FormGroup,FormControl,Validators,FormBuilder,FormArray} from '@angular/forms';

@Component({
    selector : 'page-add-tags',
    templateUrl : 'add-tags.html'

})

export class AddTagsComponent {

    public gifurl : any;
    public uploadBtn;
    public selectedIdiom = localStorage.getItem('idiom');
    addtagsForm : FormGroup; 
    addtagsForm2; 
    formgif: any;
    public gifyPath;
    constructor(public navCtrl: NavController,
                public loadingCtrl: LoadingController ,
                public toastCtrl: ToastController,
                public navparams: NavParams,
                public _uploadserv: UploadGifService,
                private file: File,
                private transfer: FileTransfer,
                public cs: CustomService,
                public fb : FormBuilder){
                //  this.gifurl = this.navparams.get('gifpath');
                //  this.selectedIdiom = this.navparams.get('idiom');
                    this.gifurl = this.navparams.get('weburl') || this.navparams.get('gifpath') ;
                    this.gifyPath = this.navparams.get('imagePath');
                    this.addtagsForm = new FormGroup({
                         url : new FormControl(this.gifurl),
                         idiom : new FormControl(this.selectedIdiom),   
                         tags : new FormControl('',[Validators.required])   
                    })
        }

    ngOnInit(){
        //  this.addtagsForm2 = this.fb.group({
        //                  url : [this.gifurl],
        //                  idiom : [this.selectedIdiom],   
        //                  tags : this.fb.array([
        //                          this.addTags(),
        //                         ]) 
        //             })
    }              

    // addTags() {
    //     // add address to the list
    //     const control = <FormArray>this.addtagsForm2.controls['addresses'];
    //     control.push(this.addtagsForm2.value.tags);
    // }      

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
  public access_token;
  uploadGifviaGallery(){
    
     console.log('uplod gifurl',this.gifurl);
    const fileTransfer: FileTransferObject = this.transfer.create();
       let filename = _.uniqueId('file_') + ".gif";
       console.log('filename',filename);
       let url = 'https://golaapi.mobigraph.co/ginger/uploadGif';
       let uploadUrl = encodeURI(url);
       this.access_token = 'Bearer ' + localStorage.getItem('access_token');
       let access_key =  'AzG7Co20vVl7cBC4Cgi1rmp7w';
       let options = {
            fileKey: 'gif',
            fileName: filename,
            mimeType: 'image/gif',
            chunkedMode: false,
            headers: {
                 'content-type' : undefined,
                 'X-Gola-Access-Key': access_key,
                 'Authorization' : this.access_token
            },
            params:{
                "gif":  filename,
                "idiom": this.selectedIdiom,
                "tags": this.addtagsForm.value.tags
            }
        }; 
         this.cs.showLoader();
         
        fileTransfer.upload(this.gifurl,url, options, false)
            .then((result: any) => {
              this.data_response = result ; 
              console.log('uploadsresults',result);
               this.presentToast();
              this.cs.hideLoader();
              this.navCtrl.push(GifDetailComponent,{
                  'url' : JSON.parse(this.data_response.response)
              });
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