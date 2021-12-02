import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRoom, Room } from '../room.model';
import { RoomService } from '../service/room.service';

@Component({
  selector: 'jhi-room-update',
  templateUrl: './room-update.component.html',
})
export class RoomUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    roomName: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(8)]],
    roomType: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(8)]],
    row: [null, [Validators.required, Validators.min(1)]],
    column: [null, [Validators.required, Validators.min(1)]],
    statusRoom: [null, [Validators.required]],
  });

  constructor(protected roomService: RoomService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ room }) => {
      this.updateForm(room);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const room = this.createFromForm();
    if (room.id !== undefined) {
      this.subscribeToSaveResponse(this.roomService.update(room));
    } else {
      this.subscribeToSaveResponse(this.roomService.create(room));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(room: IRoom): void {
    this.editForm.patchValue({
      id: room.id,
      roomName: room.roomName,
      roomType: room.roomType,
      row: room.row,
      column: room.column,
      statusRoom: room.statusRoom,
    });
  }

  protected createFromForm(): IRoom {
    return {
      ...new Room(),
      id: this.editForm.get(['id'])!.value,
      roomName: this.editForm.get(['roomName'])!.value,
      roomType: this.editForm.get(['roomType'])!.value,
      row: this.editForm.get(['row'])!.value,
      column: this.editForm.get(['column'])!.value,
      statusRoom: this.editForm.get(['statusRoom'])!.value,
    };
  }
}
