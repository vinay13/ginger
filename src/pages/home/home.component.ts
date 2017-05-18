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

@Component({
    selector : 'page-home',
    templateUrl : 'home.html'
})

export class HomeComponent implements OnInit{

  @ViewChild('mySlider') slider: Slides;
  selectedSegment: string;
  slides: any;

  public selectedIdiom;
  constructor(public navCtrl: NavController,
              public navParams : NavParams,
              public actionSheetCtrl : ActionSheetController,
              public _homeserv : HomeService,
              public cs : CustomService,
              public cameraa : Camera,
              public fileChooser : FileChooser,
              public file : File) {
              
              this.tabsData();
              this.selectedIdiom = this.navParams.get('idiom');
              this.getTrendingGIFs();
               
  this.selectedSegment = 'first';
    this.slides = [
      {
        id: "first",
        title: "The newest,most trending gifs"
      },
      {
        id: "second",
      },
      {
        id: "third",
      },
      {
          id: "fourth",
      },
      {
        id : "fifth"
      }
    ];
}
   
  searchButton(){
      this.navCtrl.push(SearchComponent);
  }

  navIdiom(){
     this.navCtrl.push(IdiomComponent);
  }

  onSegmentChanged(segmentButton) {
    console.log("Segment changed to", segmentButton.value);
    const selectedIndex = this.slides.findIndex((slide) => {
      return slide.id === segmentButton.value;
    });
    this.slider.slideTo(selectedIndex);
  }

  onSlideChanged(slider) {
    console.log('Slide changed');
    const currentSlide = this.slides[slider.activeIndex];
    this.selectedSegment = currentSlide.id;
  }

  public trendingGIFs: any;
  public gifs: Array<any> = []; 
  getTrendingGIFs(){
    this.cs.showLoader();
    this._homeserv.getTrendingGifs(this.selectedIdiom)
    .subscribe( (result) => { this.trendingGIFs = result ; this.gifs = this.gifs.concat(this.trendingGIFs.data);  this.cs.hideLoader(); },
                (err) => {  this.cs.hideLoader(); },
                () => console.log('trendingGifs',this.trendingGIFs))
  }

  checklogin(){
    this.navCtrl.push(LoginPage);
  }

  presentActionSheet(){
    let actionSheet = this.actionSheetCtrl.create({
      title: 'Upload GIF',
      buttons: [
        {
          text: 'Device Gallery',
          role: 'destructive',
          icon : 'md-document',
          handler: () => {
           // this.ImagePick();
            this.ChooseFile();
            console.log('Destructive clicked');
          }
        },{
          text: 'Web Url',
          icon: 'md-link',
          handler: () => {
            console.log('Archive clicked');
            this.navCtrl.push(UploadComponent);
          }
        }
      ]
    });
    actionSheet.present();
  }


  base64Image;
  ImageFile;
  public ImagePick(){
   
    this.cameraa.getPicture({
        destinationType: this.cameraa.DestinationType.DATA_URL,
        sourceType        : this.cameraa.PictureSourceType.PHOTOLIBRARY
    }).then((imagedata)=>{
      this.base64Image = 'data:image/jpeg;base64,' + imagedata;
      this.ImageFile = imagedata ;
       this.navCtrl.push(AddTagsComponent,{
        'gifpath' :  this.base64Image
      });    
    },(err)=>{
      console.log(err);
    });
}


  public imageFile : any;  
  public data_response; 
  ChooseFile(){
      this.fileChooser.open()
        .then(uri => {console.log(uri); this.imageFile = uri } )
        .catch(e => console.log(e));
    }


  userProfile(){
    this.navCtrl.push(ProfileComponent);
  }

  navGifDetail(url){
    this.navCtrl.push(GifDetailComponent,{
      'url' : url
    });
  }

  public tabs;
  tabsData(){
    this._homeserv.mainTabs()
    .subscribe( (res) => { this.tabs = res.main },
                () => {console.log('tabs',this.tabs)})
  }

  ngOnInit(): void {
    
  }
}