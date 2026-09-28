<?php
namespace App\Http\Controllers;

use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\View\View;

class CustomerController extends Controller
{
    /** Spring Boot APIのベースURLを取得する。 */
    function apiUrl(): string
    {
        return rtrim(config('services.java_api.url'), '/')
            . '/api/customers';
    }

    /** 顧客一覧を表示する。 */
    public function index(): View
    {
        $response = Http::acceptJson()
            ->timeout(5)
            ->get($this->apiUrl());

        if ($response->failed()) {
            abort(502, '顧客情報を取得できませんでした。');
        }

        return view('customers.index', [
            'customers' => $response->json() ?? [],
        ]);
    }

    /** 新規登録画面を表示する。 */
    public function create(): View
    {
        return view('customers.form', [
            'customer' => [],
            'isEdit' => false,
        ]);
    }

    /** 顧客を登録する。 */
    public function store(Request $request): RedirectResponse
    {
        $data = $this->validateCustomer($request);

        $response = Http::acceptJson()
            ->timeout(5)
            ->post($this->apiUrl(), $data);

        if ($response->failed()) {
            return back()
                ->withInput()
                ->withErrors([
                    'api' => '顧客を登録できませんでした。',
                ]);
        }

        return redirect()
            ->route('customers.index')
            ->with('success', '顧客を登録しました。');
    }

    /** 編集画面を表示する。 */
    public function edit(string $customerId): View
    {
        $response = Http::acceptJson()
            ->timeout(5)
            ->get($this->apiUrl() . '/' . $customerId);

        if ($response->status() === 404) {
            abort(404);
        }

        if ($response->failed()) {
            abort(502, '顧客情報を取得できませんでした。');
        }

        return view('customers.form', [
            'customer' => $response->json(),
            'isEdit' => true,
        ]);
    }

    /** 顧客情報を更新する。 */
    public function update(
        Request $request,
        string $customerId
    ): RedirectResponse {
        $data = $this->validateCustomer($request);

        $response = Http::acceptJson()
            ->timeout(5)
            ->put($this->apiUrl() . '/' . $customerId, $data);

        if ($response->status() === 404) {
            abort(404);
        }

        if ($response->failed()) {
            return back()
                ->withInput()
                ->withErrors([
                    'api' => '顧客を更新できませんでした。',
                ]);
        }

        return redirect()
            ->route('customers.index')
            ->with('success', '顧客情報を更新しました。');
    }

    /** 顧客を削除する。 */
    public function destroy(string $customerId): RedirectResponse
    {
        $response = Http::acceptJson()
            ->timeout(5)
            ->delete($this->apiUrl() . '/' . $customerId);

        if ($response->status() === 404) {
            abort(404);
        }

        if ($response->failed()) {
            return back()->withErrors([
                'api' => '顧客を削除できませんでした。',
            ]);
        }

        return redirect()
            ->route('customers.index')
            ->with('success', '顧客を削除しました。');
    }

    /**
     * 顧客情報の入力値を検証する。
     * 追加情報はJSON文字列を受け取り、配列に変換する。
     */
    private function validateCustomer(Request $request): array
    {
        $data = $request->validate([
            'customerName' => ['required', 'string', 'max:255'],
            'customerKanaName' => ['nullable', 'string', 'max:255'],
            'email' => ['nullable', 'email', 'max:255'],
            'phoneNumber' => ['nullable', 'string', 'max:255'],
            'gender' => ['nullable', 'string', 'max:255'],
            'customerItems' => ['nullable', 'json'],
        ]);

        $items = $data['customerItems'] ?? null;

        $data['customerItems'] = $items !== null && $items !== ''
            ? json_decode($items, false, 512, JSON_THROW_ON_ERROR)
            : null;

        return $data;
    }
}