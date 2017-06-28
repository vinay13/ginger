import { Component,OnInit , ViewChild } from '@angular/core';
import { NavController , Slides , ActionSheetController , NavParams } from 'ionic-angular';
import { SearchComponent } from '../search/search.component';
import { GifDetailComponent } from './gifdetail/gifdetail.component';
import { IdiomComponent } from '../idiom/idiom.component';
import { HomeService } from '../../services/home.service';
import { LoginPage } from '../login/login.component';
import { UploadComponent } from '../upload/upload.component';
import { ProfileComponent } from '../profile/profile.component';
import { CustomService } from '../../services/custom.service';
import { Camera } from '@ionic-native/camera';
import { AddTagsComponent } from '../upload/add-tags/add-tags.component';
import { FileChooser } from '@ionic-native/file-chooser';
import { File } from '@ionic-native/file';
import { AppRateService } from '../../services/apprate.service';
import { ProfileService} from '../../services/profile.service';

@Component({
    selector : 'page-home',
    templateUrl : 'home.html',
})

export class HomeComponent implements OnInit{

  @ViewChild('mySlider') slider: Slides;
  selectedSegment: string;
  slides: any;
  public selectedIdiom;
  public lang : any;  
  public currentPage = 0;
 
  public allgifs:boolean = false;
  public fallgifs:boolean = true;
  constructor(public navCtrl: NavController,
              public navParams : NavParams,
              public actionSheetCtrl : ActionSheetController,
              public _homeserv : HomeService,
              public cs : CustomService,
              public cameraa : Camera,
              public file : File,
              public fileChooser : FileChooser,
              public _proServ : ProfileService) {
              this.selectedIdiom = this.navParams.get('idiom');
            
             this.getProfileData();
              this.tabsData();
                this.tabsCatdata();
              
             // this.getTrendingGIFs();
           
            
                 this.selectedSegment = "1498143424424";
                 this.tabdataViaTabID(this.selectedSegment);
                // this.slides = [
                //     {
                //       id: "1498143424424",
                //     },
                //     {
                //       id: "def",
                //     },
                //    {
                //       id: "third",
                //     },
                //     {
                //         id: "fourth",
                //     },
                //     {
                //       id : "fifth"
                //     }
                // ];

                 //this.allgifs = '{{'+ "gif.images.downsized_still.url" +'}}';
          }

         
  // ionViewDidLoad(){
  //       setTimeout(() => {
  //         //  this.allgifs = true;
  //      //   this.fallgifs = false;
         
  //       }, 5000);
  // }

ionViewDidLoad(){
 // this.getTrendingGIFs();
}

  searchButton(){
      this.navCtrl.push(SearchComponent,{
        'idiom':this.selectedIdiom
      });
  }

  navIdiom(){
     this.navCtrl.push(IdiomComponent);
  }

  onSegmentChanged(segmentButton) {
    console.log("Segment changed to", segmentButton.value);
    const selectedIndex = this.slides.findIndex((slide) => {
      console.log('selectedIndex',selectedIndex);
      console.log('segmentButton',segmentButton.value);
      console.log('slide.id',slide.id);
      return slide.id === segmentButton.value;
    });
    this.slider.slideTo(selectedIndex);
  }

  onSlideChanged(slider) {
    console.log('Slide changed');
   
    const currentSlide = this.slides[slider.getActiveIndex()];
    this.selectedSegment = currentSlide.id;
     this.tabdataViaTabID(this.selectedSegment);
  }

  public trendingGIFs: any;
  public gifs: Array<any> = []; 
  getTrendingGIFs(){
 //this.cs.showLoader();
 console.log('currentpage',this.currentPage);
    this._homeserv.getTrendingGifs(this.selectedIdiom,this.currentPage)
    .subscribe( (result) => { this.trendingGIFs = result ; this.gifs = this.gifs.concat(this.trendingGIFs.contents); },
                (err) => { console.log(err);})
  }

  checklogin(){
    this.navCtrl.push(LoginPage,{
      'idiom': this.selectedIdiom
    });
  }

  UploadviaWeb(){
    this.cs.showLoader();
    this.navCtrl.push(UploadComponent);
    this.cs.hideLoader();
  }

  public tabdata;
  public tabsLoaded = false;
  tabsCatdata(){
     this._homeserv.getTabCategories(this.selectedIdiom)
        .subscribe( (res) => { this.slides = res.tabs;this.tabsLoaded = true;},
                    (err) => { console.log(err)},
                    () => { console.log('tabdata',this.slides[0].id)})
  }

  slideAddingID(){
    // let dis = ["first","second","third","fourth"]
    // for(let i=0; i < this.slides.length;i++)
    // {
    //   this.slides[i].add('"dis":'+ dis[i]);
    // }
  }

  public tabIddata;
  
 // public tabId = ['1498143424424','1498144144545','1498144145182','1498144145755']
 // public i = 0;
  tabdataViaTabID(tabID){
      console.log('tabiid2',tabID);
              this._homeserv.getTabDataviaTabId(this.selectedIdiom, tabID)
      .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata},
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))        
  }


  // presentActionSheet(){
  //   let actionSheet = this.actionSheetCtrl.create({
  //     title: 'Upload GIF',
  //     buttons: [
  //       {
  //         text: 'Device Gallery',
  //         role: 'destructive',
  //         icon : 'md-document',
  //         handler: () => {
  //           this.ImagePick();
  //          // this.ChooseFile();
  //           console.log('Destructive clicked');
  //         }
  //       },{
  //         text: 'Web Url',
  //         icon: 'md-link',
  //         handler: () => {
  //           console.log('Archive clicked');
  //           this.navCtrl.push(UploadComponent);
  //         }
  //       }
  //     ]
  //   });
  //   actionSheet.present();
  // }


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
//         'gifpath' :  imagedata
//       });    
//     },(err)=>{
//       console.log(err);
//       this.cs.hideLoader();
//     });
//}


  public imageFile : any;  
  public data_response; 
  ImagePick(){
    this.cs.showLoader();
      this.fileChooser.open()
        .then(uri => {console.log(uri); this.imageFile = uri ; this.navAddTag(uri); } )
        .catch(e => console.log(e));
    }

    navAddTag(uri){
    this.cs.hideLoader();
       this.navCtrl.push(AddTagsComponent,{
        'gifpath' :  uri
      });   
    }


  userProfile(){
    this.navCtrl.push(ProfileComponent);
  }

  navGifDetail(url){
    this.navCtrl.push(GifDetailComponent,{
      'url' : url,
      'idiom' : this.selectedIdiom
    });
  }

  public profiledata;
  getProfileData(){
        this._proServ.GetUserProfile()
        .subscribe( (data) => { this.profiledata = data },
                    (err) => { alert(err);},   
                    () => {console.log('pro',this.profiledata)})
  }

  public tabs;
  tabsData(){
    this._homeserv.mainTabs()
    .subscribe((res) => { this.tabs = res.main },
                () => {console.log('tabs',this.tabs)})
  }


  //  hasMoreData;
  //  doInfinite(infiniteScroll) {

  //  this.currentPage = this.currentPage + 1;
  //   console.log('currentpage', this.currentPage);
  //      this._homeserv.getTrendingGifs(this.selectedIdiom, this.currentPage).subscribe(response =>
  //       {
  //         infiniteScroll.complete();
  //         this.hasMoreData = true;
  //         this.trendingGIFs = response;
  //         this.gifs =  this.gifs.concat(this.trendingGIFs.contents); 
  //     }, 
  //   err => {
  //     infiniteScroll.complete();
  //     this.currentPage -= 1;
  //  //   this.onError(err);
  //   },
  //    () => console.log('Next Page Loading completed')
  //    );
  // }


  public pageno = 1;
  doInfinite(infiniteScroll) {

    let nextpage=this.pageno++;
    console.log("next page:"+nextpage)
    this._homeserv.getTrendingGifs(this.selectedIdiom,nextpage).subscribe(
            data => {
              infiniteScroll.complete();
                this.trendingGIFs = data;
                // for(let post of posts){
                   this.gifs =  this.gifs.concat(this.trendingGIFs.contents); 
                // }
            },
            err => {
                console.log(err);
            },
            () => console.log('Next Page Loading completed')
        );
  infiniteScroll.complete();
} 

  ngOnInit(): void {
    this.lang =  "assets/icon/ic_"+ this.selectedIdiom +".png";
    
  }
}