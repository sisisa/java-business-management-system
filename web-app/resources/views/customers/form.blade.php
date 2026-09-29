<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>{{ $isEdit ? '顧客編集' : '顧客登録' }}</title>
    <style>
        body {
            font-family: sans-serif;
            max-width: 700px;
            margin: 40px auto;
            padding: 0 20px;
            color: #1f2937;
        }

        .field { margin-bottom: 18px; }
        label { display: block; margin-bottom: 6px; font-weight: bold; }
        input, textarea {
            width: 100%;
            box-sizing: border-box;
            padding: 10px;
            border: 1px solid #d1d5db;
            border-radius: 5px;
        }
        textarea { min-height: 100px; }
        button, a {
            padding: 9px 15px;
            border: 0;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
        }
        .primary { background: #2563eb; color: white; }
        .secondary { background: #e5e7eb; color: #111827; }
        .error { color: #dc2626; font-size: 14px; }
        .actions { display: flex; gap: 10px; }
    </style>
</head>
<body>
    <h1>{{ $isEdit ? '顧客編集' : '顧客登録' }}</h1>

    @if ($errors->any())
        <div class="error">
            @foreach ($errors->all() as $error)
                <p>{{ $error }}</p>
            @endforeach
        </div>
    @endif

    <form method="POST"
          action="{{ $isEdit
              ? route('customers.update', $customer['customerId'])
              : route('customers.store') }}">

        @csrf
        @if ($isEdit)
            @method('PUT')
        @endif

        <div class="field">
            <label for="customerName">顧客名 *</label>
            <input id="customerName" name="customerName"
                   value="{{ old('customerName', $customer['customerName'] ?? '') }}"
                   required maxlength="255">
        </div>

        <div class="field">
            <label for="customerKanaName">顧客名（カナ）</label>
            <input id="customerKanaName" name="customerKanaName"
                   value="{{ old('customerKanaName', $customer['customerKanaName'] ?? '') }}"
                   maxlength="255">
        </div>

        <div class="field">
            <label for="email">メールアドレス</label>
            <input id="email" name="email" type="email"
                   value="{{ old('email', $customer['email'] ?? '') }}"
                   maxlength="255">
        </div>

        <div class="field">
            <label for="phoneNumber">電話番号</label>
            <input id="phoneNumber" name="phoneNumber"
                   value="{{ old('phoneNumber', $customer['phoneNumber'] ?? '') }}"
                   maxlength="255">
        </div>

        <div class="field">
            <label for="gender">性別</label>
            <input id="gender" name="gender"
                   value="{{ old('gender', $customer['gender'] ?? '') }}"
                   maxlength="255">
        </div>

        <div class="field">
            <label for="customerItems">追加情報（JSON形式）</label>
            <textarea id="customerItems" name="customerItems"
                      placeholder='{"industry":"IT"}'>{{ old('customerItems', isset($customer['customerItems']) && $customer['customerItems'] !== null ? json_encode($customer['customerItems'], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT) : '') }}</textarea>
        </div>

        <div class="actions">
            <button type="submit" class="primary">
                {{ $isEdit ? '更新' : '登録' }}
            </button>
            <a href="{{ route('customers.index') }}" class="secondary">
                戻る
            </a>
        </div>
    </form>
</body>
</html>
