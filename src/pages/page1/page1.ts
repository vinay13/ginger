import { Component,ViewChild,Input } from '@angular/core';
import { NavController, NavParams , ModalController , Events  } from 'ionic-angular';
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
// import Masonry from 'masonry-layout'

//lazy loading Images
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/of';
import { Observable } from 'rxjs/Observable';
import { getScrollListener } from './scroll-listener';

@Component({
  selector: 'page-page1',
  templateUrl: 'page1.html'
})

export class Page1Page {
// try{this._element.nativeElement.removeChild(element)}catch(err){};
    rootNavCtrl: NavController;
    public selectedIdiom;
    public newselectedIdiom;
    public loggedIn : boolean = false;
    @ViewChild(AngularMasonry) public masonry: AngularMasonry;
    @Input() src;
    public _msnry: any;
    lessdata;
    LessData = false;
    isSelected;
    constructor(public navCtrl: NavController,
                public navparams: NavParams,
                public _homeserv : HomeService,
                public cs : CustomService,
                public platform : Platform,
                public fileChooser : FileChooser,
                public modalCtrl : ModalController,
                public events : Events){
                             
                  this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                  this.newselectedIdiom = this.navparams.data;
                  this.selectedIdiom = this.newselectedIdiom.idiom;
                   
                platform.ready().then(() => {
                      this.tabcat();
                 })
           
                events.subscribe('lessdata:created', (user) => {
                    console.log('Welcome', user);
                    this.lessdata = user;
                    this.LessData = this.lessdata;
                });
              
                this.lessdata = localStorage.getItem('lessdata');
                  if(this.lessdata === "false"){
                    this.LessData = false
                  } 
                  else{
                    this.LessData = true;
                }              
    }

    // ionViewWillEnter(){
    // setTimeout(()=>{
    //                alert('reloaddd');
    //               this.masonry._msnry.reloadItems();
    //               this.masonry.layout();
                  
    //            //    masonry._msnry.reloadItems();
    //             }, 2500);
    //               //this.masonry._msnry.reloadItems();    

    // }

   imagePath ="https://avatars3.githubusercontent.com/u/497125?v=4&s=88";
   loadImage(imagePath: string): Observable<HTMLImageElement> {
    return Observable
        .create(observer => {
            const img = new Image();
            img.src = imagePath;
            img.onload = () => {
                observer.next(imagePath);
                observer.complete();
            };
            img.onerror = err => {
                observer.error(null);
            };
        });
}

    loadImage2(){
        const img = new Image();
      img.src= "https://avatars3.githubusercontent.com/u/497125?v=4&s=88";
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

    showPlaceholder = false;
    doStuff(){
      // alert('layout completed');
      console.log('loaded');
    //  this.showPlaceholder = !this.showPlaceholder;
    }

    
    public trendingGIFs: any;
    public gifs: Array<any> = [];
    
    public tabdata;
    public tabcat(){
      this.cs.showLoader();
         this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe((res) => { this.tabdata = res.tabs; this.cs.hideLoader();this.gettabdata(this.selectedIdiom,this.tabdata[0].id);  },
                                (err) => { console.log(err); this.cs.hideLoader();},
                                () => { console.log('tabdata',this.tabdata[0].id)})
    }


   
   // giffUrl = "item.lowResUrl || item.url ||  item.thumbNailUrl";
   abcc = "item.thumbNailUrl";
    public tabIddata;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
       //this.cs.showLoader();
       this._homeserv.getTabDataviaTabId(idiom,tabid,0)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata;  this.putinGrid();},
                  (err) => {console.log(err);},
                  () => console.log('data',this.tabIddata ))
    }

    putinGrid(){
     // for(let i=0;i<this.gifs.length;i++){
     //   console.log(this.gifs[i].lowResUrl);
     //   this.src = this.gifs;
      }
      
    

    public gifData;
    GifsViewviaId(tabid){
      this.cs.showLoader();
      this._homeserv.getGifviaID(tabid)
          .subscribe((res) => { this.gifData = res; this.cs.hideLoader(); this.navGifDetail(res);},
                        (err) => { this.cs.hideLoader();} )
 
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

     public newlayout() {
        setTimeout(() => {
            this.masonry._msnry.layout();
        },1000);

        // console.log('AngularMasonry:', 'Layout');
    }

    ionViewWillEnter (){
      this.newlayout();
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
          this.gifs =  this.gifs.concat(data); 
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
