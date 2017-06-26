import { Component,OnInit } from '@angular/core';
import { NavController, NavParams ,Events } from 'ionic-angular';
import { HomeService } from '../../services/home.service';

@Component({
  selector: 'page-page2',
  templateUrl: 'page2.html'
})
export class Page2Page implements OnInit{
    // celebrity page
   public tabId;
   public selectedIdiom = "Tamil";
   public tabIddata; 
     // public tabId = 1498144144545;
      
    constructor(public navCtrl: NavController, 
                public navparams: NavParams,
                public _homeserv : HomeService,
                public events : Events) {
                  this.selectedIdiom = this.navparams.get('idiom');
                        this.events.subscribe('tab:selected',(id) => {
                    this.tabId = id;
                //   this.getTabdata(this.selectedIdiom,this.tabId);
                this._homeserv.getTabDataviaTabId(this.selectedIdiom,this.tabId)
      .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata},
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
    
                   console.log('tabId',this.tabId);
                 });
                }
                
    public trendingGIFs: any;
    public gifs: Array<any> = [];
    
   
    
   

     ionViewDidEnter(){
      

     }

    ngOnInit(){
      
    }
}
