import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopOverComponent } from './popover';
import { SearchComponent } from '../../search/search.component';
import { SearchResultComponent } from '../../search/searchResult/search-result.component';
import { Nav,NavController, ToastController , NavParams ,Events} from 'ionic-angular';
import { SocialSharing } from '@ionic-native/social-sharing';
import { CustomService } from '../../../services/custom.service';
import { HomeService } from '../../../services/home.service';
import { File } from '@ionic-native/file';
import { ProfileComponent } from '../../profile/profile.component';
import { Clipboard } from '@ionic-native/clipboard';
import { Page1Page } from '../../page1/page1.ts';
import {AboutPage} from '../../about/about.ts';

@Component({
    selector : 'page-gifdetail',
    templateUrl : 'gifdetail.html'
})

export class GifDetailComponent {

  //  rootPage:any = AboutPage;
    public gifurl;
    public gifobject;
    public recomns : any;
    public tagslist;
    public selectedIdiom;
    public scount;
    lessdata;
    LessData;
 
    constructor(public popoverCtrl : PopoverController,
                public navCtrl : NavController,
                public toastCtrl : ToastController,
                private socialSharing: SocialSharing,
                private _homeserv: HomeService,
                private cs : CustomService,
                private file: File,
                private clipboard: Clipboard,
                public navparams : NavParams,
                public events : Events,
                public nav : Nav
               ){
                    this.gifobject = this.navparams.get('url');
                     this.selectedIdiom = this.navparams.get('idiom') || localStorage.getItem('idiom');
                     //this.gifurl =  this.gifobject.url;
                    console.log('gifobject',this.gifobject);
                    console.log('gifobject id', this.gifobject.id)
                    this.scount = this.gifobject.shareCount ;
                    this.RecommendedGifs();
                    this.GetUsername();
                 //   this.webintent();
                    this.tagslist = this.gifobject.tags;
                    console.log('tagslist',this.tagslist);


                    //    let el = document.getElementsByClassName('scroll-content')[0] as HTMLElement;
                    //     this.recomns.viewHeight = el.offsetHeight;


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

    loadProgress: number = 0;
    public hidebar = false;	            
    ionViewDidLoad(){

		setInterval(() => {

			if(this.loadProgress < 100){
				this.loadProgress++;
                
                if(this.loadProgress == 1){
                    this.gifurl = this.gifobject.lowResUrl;
                }

                if(this.loadProgress == 33)
                {
                     this.gifurl = this.gifobject.url;
                }
                 
                if(this.loadProgress == 99)
                {
                    this.hidebar = true;
                }
			}

		},50);
	} 


setBackground(){
    document.body.style.backgroundImage = 'url(https://thumbs.dreamstime.com/t/illustrate-blank-green-board-wooden-frame-38880868.jpg)';
}
 
    poptoHome(){
        // this.navCtrl.setRoot(AboutPage,{
        //     'idiom': this.selectedIdiom
        // });

        this.nav.popToRoot();
    }

    hidetags = true;
    showtags = false;
    expandTags(){ 
        this.hidetags = !this.hidetags; 
        this.showtags = !this.showtags;
    }
  

    golauser;
    guser;
    public GetUsername(){
      this.guser =  this.gifobject.publishedBy;
      this.golauser  = this.guser.split('@',1)
         
    } 
    
    public totalcount;
    gifId;
    pageno = 0;
    RecommendedGifs(){
     //   this.cs.showLoader();
         this.gifId = this.gifobject.gifId || this.gifobject.id;
        this._homeserv.getRelatedGifs(this.selectedIdiom,this.gifId,this.pageno)
            .subscribe( (res) => {this.recomns = res.contents, this.totalcount = res.totalCount},
                        (err) => {console.log(err)},
                        () => console.log('related gifs',this.totalcount))
    }

    presentPopover(myEvent,gifurl){
        let popover = this.popoverCtrl.create(PopOverComponent,{gifURL : gifurl});
        console.log('popOver',myEvent);
        console.log('popgifurl',gifurl),
        popover.present({
            ev: myEvent,
        });
    }

    TagClicked(tag){
        this.navCtrl.push(SearchResultComponent,{
            'tag' : tag,
            'idiom': this.selectedIdiom
        });
    }

    searchButton(){
      this.navCtrl.push(SearchComponent);
    }

    userProfile(){
        this.navCtrl.push(ProfileComponent,{
            'email' : this.guser
        });
    }

    GIFviewer(url){
        this.navCtrl.push(GifDetailComponent,{
            'url': url,
            'idiom':this.selectedIdiom
        });   
    }

    public favorite = false;
    thefavorites;
    Addfavorites(){
        this._homeserv.favoritesGifs(this.gifId)
        .subscribe( (res) => { this.thefavorites = res },
                    (err) => { console.log(err)})
       this.favorite = true;
       this.presentToast();
    }

    copyUrl(){
        this.clipboard.copy(this.gifurl).then(
                 (resolve: string) => {
                         alert(resolve);
             },
             (reject: string) => {
         }
        )

        let toast = this.toastCtrl.create({
            message : 'URL copied to clipboard',
            duration : 3000
        });
        toast.present();
    }


    // webintent(){
    //     const options = {
    //         action: this.webIntent.ACTION_VIEW,
    //         url: this.gifId,
    //         type: 'application/vnd.android.package-archive'
    // };

    // this.webIntent.startActivity(options).then(() => {
    //   alert("Success. URL: " + this.gifId);
    // },  () => {
    //   alert("Failed to open URL via Android Intent. URL: " + this.gifId)});

    // }


    presentToast(){
        let toast = this.toastCtrl.create({
            message: 'Added to favorites list',
            duration: 3000
        });
        toast.present();
    }

    public giffSrc;
    sourceClick(){
        let link = 'http://'+this.gifobject.source;
        this.giffSrc = link;
    }

    // scount;
    // sharesCountDetails(){
    //     this._homeserv.sharesdetails(this.gifId)
    //         .subscribe((data) => { this.scount = data; this.scount = this.scount.shareCount;},
    //                     (err) => { console.log(err)},
    //                     () => { console.log('shareCount',this.scount)} )
    // }

    shared;
    share(){
        this._homeserv.shareArbit(this.gifId)
            .subscribe( (data) => { this.shared = data;},
                        (err) => { console.log('share',err)},
                        () => { console.log('shared',this.shared)})
    }

    downloadToast(){
        let toast = this.toastCtrl.create({
            message: 'GIF Saved!',
            duration: 3000
        });
        toast.present();
    }

   golalinkk = "http://gola.desi";
   shareGif(){
     this.cs.showLoader();
     this.socialSharing.shareViaWhatsApp("",this.gifurl, this.golalinkk)
       .then(()=>{
         this.share();
         this.cs.hideLoader();
       },
       ()=>{
          this.cs.hideLoader();
       })

      // this.socialSharing.share()
    }

  shareGifTwitter(){
    this.cs.showLoader();
    this.socialSharing.shareViaTwitter("",this.gifurl, this.golalinkk)
        .then(()=>{
          this.cs.hideLoader();
        },
        ()=>{
          alert("U don't have twitter app");
          this.cs.hideLoader();
        })
    }

  shareGifFacebook(){
      this.cs.showLoader();
      this.socialSharing.shareViaFacebook("",this.gifurl, this.golalinkk)
        .then(() => {
            this.cs.hideLoader();
        },
        () => { alert("U don't have facebook app"); this.cs.hideLoader(); } )
  }

  sharegifviaHike(){
      this.cs.showLoader();
      this.socialSharing.shareVia('hike',"","fds",this.gifurl,this.golalinkk)
      .then(() => {
          this.cs.hideLoader();
      },
      (err) => { alert('u dont have hike app'); this.cs.hideLoader();})
  }

  sharegifviaMessenger(){
      this.cs.showLoader();
      this.socialSharing.shareVia('com.facebook.orca','themessage','gdgf',this.gifurl,this.golalinkk)
      .then(() => {
          this.cs.hideLoader();
      },
      (err) => {  alert(err);alert('YOU dont have Messenger app'); this.cs.hideLoader();})
  }


  shareGifInstagram(){
      this.cs.showLoader();
      this.socialSharing.shareViaInstagram("message", this.gifurl)
            .then(() =>{
                this.cs.hideLoader();
            },
           ()=> { alert("U don't have Instagram app"); this.cs.hideLoader(); })
  }

  shareSheet(){
      this.cs.showLoader();
      this.socialSharing.share("",'gola',this.gifurl, this.golalinkk)
        .then( () =>{
            this.cs.hideLoader();
        },
        () => { this.cs.hideLoader(); }) 
  }
    
//   public trendingGIFs;
//   doInfinite(infiniteScroll) {

//     let nextpage=this.pageno++;
//     this._homeserv.getRelatedGifs(this.selectedIdiom,this.gifId,this.pageno).subscribe(
//             data => {
//                 let posts=  data.contents;
//                 for(let post of posts){
//                     // console.log(post);
//                     this.recomns.push(post); 
//                 }
//                   //  this.recomns = this.recomns.push(data.contents);
//             },
//             err => {
//                 console.log(err);
//             },
//             () => console.log('Next Page Loading completed')
//         );
//   infiniteScroll.complete();
// }

currentPage = 0;
 doInfinite(infiniteScroll) {

   this.currentPage = this.currentPage + 1;
    console.log('currentpage', this.currentPage);
       this._homeserv.getRelatedGifs(this.selectedIdiom,this.gifId,this.currentPage).subscribe(data =>
        {
          infiniteScroll.complete();
        //   this.hasMoreData = true;
        //   this.trendingGIFs = data;
          this.recomns =  this.recomns.concat(data.contents); 
      }, 
    err => {
      infiniteScroll.complete();
      this.currentPage -= 1;
   //   this.onError(err);
    },
     () => console.log('Next Page Loading completed')
     );
  }


//download(){
//      const fileTransfer: TransferObject = this.transfer.create();
//      this.cs.showLoader();
//      // const imageLocation = `${cordova.file.applicationDirectory}www/assets/img/${image}`;
//      fileTransfer.download( this.gifurl,this.file.applicationDirectory+'ginger'+'aa.gif').then((entry) => {
//      //fileTransfer.download(url, cordova.file.externalRootDirectory + {{appName}} + 'filename')
//      this.cs.hideLoader();
//      this.downloadToast(); 
//     }, (error) => {
//       alert('err');
//       this.cs.hideLoader();
//     });
//   }
}