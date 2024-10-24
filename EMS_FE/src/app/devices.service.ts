import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DevicesService {

  constructor() { }
  private selectedDeviceIdSubject = new BehaviorSubject<number | null>(null);
  selectedDeviceId$ = this.selectedDeviceIdSubject.asObservable();

  setSelectedDeviceId(deviceId: number | null) {
    this.selectedDeviceIdSubject.next(deviceId);
  }
}
