import { Component,Input, Output, EventEmitter } from '@angular/core';
import { IonicPage, NavController,NavParams , Events } from 'ionic-angular';
import { Page1Page } from "../page1/page1";
import { Page2Page } from "../page2/page2";
import { Page3Page } from "../page3/page3";
import { HomeService } from '../../services/home.service';
import { IdiomComponent } from '../idiom/idiom.component';
import { SearchComponent } from '../search/search.component';
// import { SuperTabsController } from 'ionic2-super-tabs';
// import { SuperTabsController } from '../../ionic2-super-tabs/src';

// @IonicPage({
//   segment: 'about/:type'
// }) 

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})

export class AboutPage {

  public page1: any = Page1Page;
  public page2: any = Page2Page;
  public page3: any = Page3Page;
 // public page4: any = this.Page4Page;

  showIcons: boolean = false;
  showTitles: boolean = true;
  pageTitle: string = 'abc';
  title;
  public tabdata;
  public trendingGIFs: any;
  public gifs: Array<any> = [];
  public selectedIdiom = "Hindi"; 

  constructor(public navCtrl: NavController, 
              private navParams: NavParams,
              public _homeserv : HomeService,
              public events: Events) {
                  this.title = 'vinay';
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

  ionViewDidLoad(){
    this._homeserv.getTabCategories(this.selectedIdiom)
        .subscribe( (res) => { this.tabdata = res.tabs; this.tabsLoaded = true; },
                    (err) => { console.log(err)},
                    () => { console.log('tabdata',this.tabdata)})

     
  }


  ngAfterViewInit() {
      
    // this.superTabsCtrl.increaseBadge('page1', 10);
    // this.superTabsCtrl.enableTabSwipe('page3', false);
    // this.superTabsCtrl.enableTabsSwipe(false);
  }

  // public tabsCategories(){
  //   this._homeserv.getTabCategories(this.selectedIdiom)
  //       .subscribe( (res) => { this.tabdata = res.tabs },
  //                   (err) => { console.log(err)},
  //                   () => { console.log('tabdata',this.tabdata)})
  // }

  onTabSelect(tab: { index: number; id: string; }){
      console.log(`Selected tab: `, tab);
      console.log('tabbb',tab.id);
       this.events.publish('tab:selected', tab.id);
  }

    searchButton(){
      this.navCtrl.push(SearchComponent,{
        'idiom':this.selectedIdiom
      });
  }

  navIdiom(){
     this.navCtrl.push(IdiomComponent);
  }
}