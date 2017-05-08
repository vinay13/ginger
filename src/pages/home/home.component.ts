import { Component,OnInit , ViewChild } from '@angular/core';
import { NavController , Slides , ActionSheetController , NavParams } from 'ionic-angular';
import { SearchComponent } from '../search/search.component';
import { GifDetailComponent } from './gifdetail/gifdetail.component';
import { IdiomComponent } from '../idiom/idiom.component';

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
              public actionSheetCtrl : ActionSheetController) {

         this.selectedIdiom =  this.navParams.get('idiom');

                  
  this.selectedSegment = 'first';
    this.slides = [
      {
        id: "first",
        title: "First Slide"
      },
      {
        id: "second",
        title: "Second Slide"
      },
      {
        id: "third",
        title: "Third Slide"
      },
      {
          id: "fourth",
          title: "fourth slide"
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




  presentActionSheet() {
    let actionSheet = this.actionSheetCtrl.create({
      title: 'Upload GIF',
      buttons: [
        {
          text: 'Device Gallery',
          role: 'destructive',
          icon : 'md-document',
          handler: () => {
            console.log('Destructive clicked');
          }
        },{
          text: 'Web Url',
          icon: 'md-link',
          handler: () => {
            console.log('Archive clicked');
          }
        }
      ]
    });
    actionSheet.present();
  }


  navGifDetail(){
    this.navCtrl.push(GifDetailComponent);
  }


    ngOnInit(): void {}
}