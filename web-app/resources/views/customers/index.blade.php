<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>顧客管理</title>
    <style>
        body {
            font-family: sans-serif;
            max-width: 1100px;
            margin: 40px auto;
            padding: 0 20px;
            color: #1f2937;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th { background: #f3f4f6; }

        a, button {
            padding: 7px 12px;
            border: 0;
            border-radius: 5px;
            text-decoration: none;
            cursor: pointer;
            font-size: 14px;
        }

        .primary { background: #2563eb; color: white; }
        .edit { background: #e5e7eb; color: #111827; }
        .delete { background: #dc2626; color: white; }
        .message { padding: 12px; background: #dcfce7; }
        .error { padding: 12px; background: #fee2e2; }
        .actions { display: flex; gap: 6px; }
        form { margin: 0; }
    </style>
</head>
<body>
    <div class="header">
        <h1>顧客管理</h1>
        <a href="{{ route('customers.create') }}" class="primary">
            新規登録
        </a>
    </div>

    @if (session('success'))
        <p class="message">{{ session('success') }}</p>
    @endif

    @if ($errors->any())
        <div class="error">
            @foreach ($errors->all() as $error)
                <p>{{ $error }}</p>
            @endforeach
        </div>
    @endif

    <table>
        <thead>
            <tr>
                <th>顧客名</th>
                <th>カナ</th>
                <th>メールアドレス</th>
                <th>電話番号</th>
                <th>性別</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            @forelse ($customers as $customer)
                <tr>
                    <td>{{ $customer['customerName'] ?? '' }}</td>
                    <td>{{ $customer['customerKanaName'] ?? '' }}</td>
                    <td>{{ $customer['email'] ?? '' }}</td>
                    <td>{{ $customer['phoneNumber'] ?? '' }}</td>
                    <td>{{ $customer['gender'] ?? '' }}</td>
                    <td>
                        <div class="actions">
                            <a class="edit"
                               href="{{ route('customers.edit', $customer['customerId']) }}">
                                編集
                            </a>

                            <form method="POST"
                                  action="{{ route('customers.destroy', $customer['customerId']) }}"
                                  onsubmit="return confirm('この顧客を削除しますか？')">
                                @csrf
                                @method('DELETE')
                                <button type="submit" class="delete">
                                    削除
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
            @empty
                <tr>
                    <td colspan="6">顧客情報がありません。</td>
                </tr>
            @endforelse
        </tbody>
    </table>
</body>
</html>
