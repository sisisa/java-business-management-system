<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CustomerController;

// 顧客管理画面
Route::prefix('customers')->name('customers.')->group(function () {
    Route::get('/', [CustomerController::class, 'index'])
        ->name('index');

    Route::get('/create', [CustomerController::class, 'create'])
        ->name('create');

    Route::post('/', [CustomerController::class, 'store'])
        ->name('store');

    Route::get('/{customerId}/edit', [CustomerController::class, 'edit'])
        ->name('edit');

    Route::put('/{customerId}', [CustomerController::class, 'update'])
        ->name('update');

    Route::delete('/{customerId}', [CustomerController::class, 'destroy'])
        ->name('destroy');
});

Route::get('/', function () {
    return view('welcome');
});
