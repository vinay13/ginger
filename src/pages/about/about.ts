import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { IonicPage, NavController,NavParams , Events } from 'ionic-angular';
import { Page1Page } from "../page1/page1";
import { Page2Page } from "../page2/page2";
import { Page3Page } from "../page3/page3";
import { HomeService } from '../../services/home.service';
import { IdiomComponent } from '../idiom/idiom.component';
import { SearchComponent } from '../search/search.component';
import { LoginPage } from '../login/login.component';
// import { SuperTabsController } from 'ionic2-super-tabs';
// import { SuperTabsController } from '../../ionic2-super-tabs/src';

// @IonicPage({
//   segment: 'about/:type'
// }) 

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})

export class AboutPage implements OnInit {

  public page1: any = Page1Page;
  public page2: any = Page2Page;
  public page3: any = Page3Page;
 // public page4: any = this.Page4Page;

  showIcons: boolean = false;
  showTitles: boolean = true;
  pageTitle: string = 'abc';
  title;
  public lang;
  public tabdata;
  public trendingGIFs: any;
  public gifs: Array<any> = [];
  public selectedIdiom;
   idiom3;
idiomdict;
// @Output()
// tabSelect: EventEmitter<any> = new EventEmitter<any>();
//  onTabSelect(index: number) {
//     this.tabSelect.emit(index);
//   }
  constructor(public navCtrl: NavController, 
              private navParams: NavParams,
              public _homeserv : HomeService,
              public events: Events) {

                    
                  
                  this.selectedIdiom = this.navParams.get('idiom');
                 
                  //testing

                    this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe( (res) => { this.tabdata = res.tabs; this.tabdata.splice(0,1);this.onTabSelect(this.tabdata[0]);this.abcetc(this.tabdata[0].id);this.tabsLoaded = true;    },
                    (err) => { console.log(err)},
                    () => { console.log('tabdata',this.tabdata[0].id)})

                  //end here
                  console.log('aboutpageIdiom',this.selectedIdiom);
                 // this.events.publish('idiom:selected',  this.selectedIdiom);
              //   this.tabsCategories();
  const type = navParams.get('type');
    switch (type) {
      case 'icons-only':
        this.showTitles = false;
        this.pageTitle += ' - Icons only';
        break;

      case 'titles-only':
        this.showIcons = true;
        this.pageTitle += ' - Titles only';
        break;
    }
  }

  tabsLoaded =  false;

  ngOnInit(){
    this.lang =  "assets/icon/ic_"+ this.selectedIdiom +".png";
  

  }

  abcetc(tbid){
    console.log('abctabid',tbid);
       this.idiomdict = {
                      'idiom': this.selectedIdiom,
                      'index' : 0,
                      'tabid' : tbid
                   }
     this.idiom3 = this.idiomdict;
   //this.gettabdata(this.selectedIdiom,tbid);           
  }

  arbittt(ftdataa){
  
  }
 

     checklogin(){
    this.navCtrl.push(LoginPage,{
      'idiom': this.selectedIdiom
    });
  }

  ngAfterViewInit() {
      
    // this.superTabsCtrl.increaseBadge('page1', 10);
    // this.superTabsCtrl.enableTabSwipe('page3', false);
    // this.superTabsCtrl.enableTabsSwipe(false);
  }

   slideTo(tabsId){
     console.log('in slide to fun',tabsId);
   }
  

  onTabSelect(ev : any){
     this.events.publish('tab:selected', ev.id);
   //  alert('pub');
  }

  public tabIddata;
  gettabdata(idiom,tabid){
     this.tabIddata = [];
     this._homeserv.getTabDataviaTabId(idiom,tabid)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
}

    searchButton(){
      this.navCtrl.push(SearchComponent,{
        'idiom':this.selectedIdiom
      });
  }

  navIdiom(){
     this.navCtrl.push(IdiomComponent);
  }



  // ngOnInit(): void {
    
    
  // }
}