import { Component,OnInit , ViewChild } from '@angular/core';
import { NavController , Slides , ActionSheetController } from 'ionic-angular';
import { SearchComponent } from '../search/search.component';

@Component({
    selector : 'page-home',
    templateUrl : 'home.html'
})

export class HomeComponent implements OnInit{

      @ViewChild('mySlider') slider: Slides;
  selectedSegment: string;
  slides: any;

  constructor(public navCtrl: NavController,
                public actionSheetCtrl : ActionSheetController) {

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
      }
    ];
  }

  searchButton(){
      this.navCtrl.push(SearchComponent);
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



    ngOnInit(): void {}
}