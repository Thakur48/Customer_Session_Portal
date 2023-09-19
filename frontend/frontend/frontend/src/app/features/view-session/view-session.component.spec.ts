import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewSessionComponent } from './view-session.component';
describe('ViewSessionComponent', () => {
  let component: ViewSessionComponent;
  let fixture: ComponentFixture<ViewSessionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewSessionComponent]
    });
    fixture = TestBed.createComponent(ViewSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
