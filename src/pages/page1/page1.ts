import { Component,ViewChild } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';
import { CustomService } from '../../services/custom.service';
import { UploadComponent } from '../upload/upload.component';
import { LoginPage} from '../login/login.component';
import { FileChooser } from '@ionic-native/file-chooser';
import { AddTagsComponent } from '../upload/add-tags/add-tags.component';
import {AppRate} from '@ionic-native/app-rate';
import { Platform } from 'ionic-angular';
 import { MasonryModule,AngularMasonry } from 'angular2-masonry';
// let AngularMasonry;

@Component({
  selector: 'page-page1',
  templateUrl: 'page1.html'
})

export class Page1Page {
 
    rootNavCtrl: NavController;
    public selectedIdiom;
    public newselectedIdiom;
    public loggedIn : boolean = false;
   //@ViewChild(MasonryModule) private masonry: MasonryModule;
   @ViewChild(AngularMasonry) private masonry: AngularMasonry;
  
    constructor(public navCtrl: NavController,
                public navparams: NavParams,
                public _homeserv : HomeService,
                public cs : CustomService,
                public platform : Platform,
                public fileChooser : FileChooser){
                             
                  this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                  this.newselectedIdiom = this.navparams.data;
                  this.selectedIdiom = this.newselectedIdiom.idiom;
             
                  platform.ready().then(() => {
                      this.tabcat();
                  })
                        
    }


    checkUserLogin(){
     let token = localStorage.getItem('access_token');
     console.log('token',token);
     if(token != null){
        this.UploadviaWeb();
     }else{
       this.rootNavCtrl.push(LoginPage);
     }
   
    }

    doStuff(){
      //alert('layout completed');
    }

    
    public trendingGIFs: any;
    public gifs: Array<any> = [];
  
    public tabdata;
    public tabcat(){
         this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe((res) => { this.tabdata = res.tabs; this.gettabdata(this.selectedIdiom,this.tabdata[0].id);  },
                                (err) => { console.log(err)},
                                () => { console.log('tabdata',this.tabdata[0].id)})
    }

    public tabIddata;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
       this._homeserv.getTabDataviaTabId(idiom,tabid,0)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
    }

    public gifData;
    GifsViewviaId(tabid){
      this._homeserv.getGifviaID(tabid)
          .subscribe((res) => { this.gifData = res; this.navGifDetail(res);})
    }

    navGifDetail(url){
      this.rootNavCtrl.push(GifDetailComponent,{
        'url' : url,
        'idiom' : this.selectedIdiom
      });
    }

    UploadviaWeb(){
      this.cs.showLoader();
      this.rootNavCtrl.push(UploadComponent);
      this.cs.hideLoader();
    }

  public imageFile : any;  
  public data_response; 
  ImagePick(){
      this.fileChooser.open()
        .then(uri => {console.log(uri); this.imageFile = uri ; this.navAddTag(uri); } )
        .catch(e => console.log(e));
  }

  navAddTag(uri){
       this.rootNavCtrl.push(AddTagsComponent,{
        'gifpath' :  uri
      });   
    }

    ionViewWillEnter (){
      setTimeout(() => { 
                     this.masonry._msnry.reloadItems()
              },3000)
    }

    
//   base64Image;
//   ImageFile;
//   public ImagePick(){
//    this.cs.showLoader();
//     this.cameraa.getPicture({
//         destinationType: this.cameraa.DestinationType.DATA_URL,
//         mediaType : this.cameraa.MediaType.ALLMEDIA,
//         sourceType     : this.cameraa.PictureSourceType.SAVEDPHOTOALBUM
//     }).then((imagedata)=>{
//       this.base64Image = 'data:image/gif;base64' + imagedata;
//       this.ImageFile = imagedata ;
//       this.cs.hideLoader();
//        this.navCtrl.push(AddTagsComponent,{
//         'gifpath' :  this.base64Image
//       });    
//     },(err)=>{
//       console.log(err);
//       this.cs.hideLoader();
//     });
// }



currentPage = 0;
 doInfinite(infiniteScroll) {

   this.currentPage = this.currentPage + 1;
    console.log('currentpage', this.currentPage);
       this._homeserv.getTabDataviaTabId(this.selectedIdiom,this.tabdata[0].id,this.currentPage).subscribe(data =>
        {
          infiniteScroll.complete();
        //   this.hasMoreData = true;
        //   this.trendingGIFs = data;
          this.tabIddata =  this.tabIddata.concat(data); 
      }, 
    err => {
      infiniteScroll.complete();
      this.currentPage -= 1;
   //   this.onError(err);
    },
     () => console.log('Next Page Loading completed')
     );
  } 
     

}
